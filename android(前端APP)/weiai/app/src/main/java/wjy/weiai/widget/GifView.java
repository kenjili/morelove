package wjy.weiai.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.io.InputStream;
import java.util.Vector;

public class GifView extends ImageView {

    private GifPlayer gp;
    private Bitmap bm;
    private Handler handler = new Handler();

    private Runnable playFrame = new Runnable() {
        @Override
        public void run() {
            if (null != bm && !bm.isRecycled()) {
                GifView.this.setImageBitmap(bm);
            }
        }
    };

    public GifView(Context context) {
        super(context);
    }

    public GifView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public GifView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    /**
     * 播放gif
     */
    public void play(InputStream is) {
        gp = new GifPlayer();
        gp.read(is);
        if (mPlayGifThread != null) {
            mPlayGifThread.stopPaly();
            mPlayGifThread = null;
        }
        mPlayGifThread = new PlayGifThread();
        mPlayGifThread.start();
    }

    /**
     * 停止播放
     */
    public void stop() {
        if (mPlayGifThread != null) {
            mPlayGifThread.stopPaly();
            mPlayGifThread = null;
        }
    }

    private PlayGifThread mPlayGifThread = null;

    private class PlayGifThread extends Thread {

        private boolean isRun = false;

        public void run() {
            isRun = true;
            final int frameCount = gp.getFrameCount();
            final int loopCount = gp.getLoopCount();
            int repetitionCounter = 0;
            do {
                for (int i = 0; i < frameCount; i++) {
                    if (!isRun) {
                        bm = gp.getFrame(0);
                        handler.post(playFrame);
                        return;
                    }
                    bm = gp.getFrame(i);
                    int t = gp.getDelay(i);
                    handler.post(playFrame);
                    try {
                        Thread.sleep(t);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (0 != loopCount) {
                    repetitionCounter++;
                    if (repetitionCounter == loopCount)
                        break;
                }
            } while (isRun);

        }

        public void stopPaly() {
            isRun = false;
        }
    }


    public class GifPlayer {

        /**
         * 没有错误（读取与解码成功）
         */
        public static final int STATUS_OK = 0;

        /**
         * 文件解码错误（可能部分解码错误）
         */
        public static final int STATUS_FORMAT_ERROR = 1;

        /**
         * 文件读取状态：无法打开资源文件。
         */
        public static final int STATUS_OPEN_ERROR = 2;

        /**
         * 解码器像素堆栈最大大小
         */
        protected static final int MAX_STACK_SIZE = 4096;

        protected InputStream in; //文件输入流

        protected int status;  //读取与解码过程状态

        protected int width; // 图形的宽

        protected int height; // 填充图形的高

        protected boolean gctFlag; // 全局颜色表标志

        protected int gctSize; // 全局颜色表的大小

        protected int loopCount = 1; // 迭代；0 =永远重复

        protected int[] gct; // 全局色表

        protected int[] lct; // 局部色表

        protected int[] act; // 激活色表

        protected int bgIndex; // 背景颜色索引

        protected int bgColor; // 背景颜色

        protected int lastBgColor; // 上一次背景颜色

        protected int pixelAspect; // 像素纵横比

        protected boolean lctFlag; // 局部色表标志

        protected boolean interlace; // interlace flag

        protected int lctSize; // 局部色表大小

        protected int ix, iy, iw, ih; // 当前图像矩形

        protected int lrx, lry, lrw, lrh; //上一个图像矩形

        protected Bitmap image; // 当前图像帧

        protected Bitmap lastBitmap; // 前一帧

        protected byte[] block = new byte[256]; // 当前块

        protected int blockSize = 0; // 块大小最后一个图形控件扩展信息

        protected int dispose = 0; // 0=no action; 1=leave in place; 2=restore to
        // bg; 3=restore to prev

        protected int lastDispose = 0;

        protected boolean transparency = false; // 是否使用透明色

        protected int delay = 0;

        protected int transIndex; // 透明色索引

        //解码器工作阵列
        protected short[] prefix;

        protected byte[] suffix;

        protected byte[] pixelStack;

        protected byte[] pixels;

        protected Vector<GifFrame> frames; // 从当前文件读取的帧

        protected int frameCount; //帧总数

        /**
         * 帧结构
         */
        class GifFrame {
            public Bitmap image;
            public int delay; //延迟 （该帧持续多少毫秒秒）

            public GifFrame(Bitmap im, int del) {
                image = im;
                delay = del;
            }
        }

        /**
         * 获取指定帧的显示时间。
         *
         * @param n 帧指数
         * @return 显示时间（毫秒）
         */
        public int getDelay(int n) {
            delay = -1;
            if ((n >= 0) && (n < frameCount)) {
                delay = frames.elementAt(n).delay;
            }
            return delay;

        }

        /**
         * 获取从文件读取的帧的数目。
         *
         * @return 帧的总数
         */
        public int getFrameCount() {

            return frameCount;

        }

        /**
         * 获取gif图像的第一帧（或唯一帧）的图像内容。
         *
         * @return 如果没有则返回NULL
         */
        public Bitmap getBitmap() {

            return getFrame(0);

        }

        /**
         * 得到gif的重复播放次数,计数0意味着死循环重复
         *
         * @return 返回迭代次数，如果loopCount为1（1为只播放一次） loopCount为0（死循环重复）
         * loopCount为n 循环次
         */
        public int getLoopCount() {
            return loopCount;
        }

        /**
         * Creates new frame image from current data (and previous frames as
         * specified by their disposition codes).
         */
        protected void setPixels() {

            // 目标图像的像素数组
            int[] dest = new int[width * height];

            // fill in starting image contents based on last image's dispose code

            if (lastDispose > 0) {

                if (lastDispose == 3) {

                    // use image before last

                    int n = frameCount - 2;

                    if (n > 0) {

                        lastBitmap = getFrame(n - 1);

                    } else {

                        lastBitmap = null;

                    }

                }

                if (lastBitmap != null) {

                    lastBitmap.getPixels(dest, 0, width, 0, 0, width, height);

                    // copy pixels

                    if (lastDispose == 2) {

                        // fill last image rect area with background color

                        int c = 0;

                        if (!transparency) {

                            c = lastBgColor;

                        }

                        for (int i = 0; i < lrh; i++) {

                            int n1 = (lry + i) * width + lrx;

                            int n2 = n1 + lrw;

                            for (int k = n1; k < n2; k++) {

                                dest[k] = c;

                            }

                        }

                    }

                }

            }

            // copy each source line to the appropriate place in the destination

            int pass = 1;

            int inc = 8;

            int iline = 0;

            for (int i = 0; i < ih; i++) {

                int line = i;

                if (interlace) {

                    if (iline >= ih) {

                        pass++;

                        switch (pass) {

                            case 2:

                                iline = 4;

                                break;

                            case 3:

                                iline = 2;

                                inc = 4;

                                break;

                            case 4:

                                iline = 1;

                                inc = 2;

                                break;

                            default:

                                break;

                        }

                    }

                    line = iline;

                    iline += inc;

                }

                line += iy;

                if (line < height) {

                    int k = line * width;

                    int dx = k + ix; // start of line in dest

                    int dlim = dx + iw; // end of dest line

                    if ((k + width) < dlim) {

                        dlim = k + width; // past dest edge

                    }

                    int sx = i * iw; // start of line in source

                    while (dx < dlim) {

                        // map color and insert in destination

                        int index = ((int) pixels[sx++]) & 0xff;

                        int c = act[index];

                        if (c != 0) {

                            dest[dx] = c;

                        }

                        dx++;

                    }

                }

            }

            image = Bitmap.createBitmap(dest, width, height, Bitmap.Config.ARGB_4444);

        }

        /**
         * 获取帧的图像内容。
         */
        public Bitmap getFrame(int n) {
            if (frameCount <= 0)
                return null;
            n = n % frameCount;  //取余数是为了n大于总数情况下的循环
            return ((GifFrame) frames.elementAt(n)).image;
        }

        /**
         * 从流中读取GIF图像
         *
         * @param is gif文件流
         * @return 读取状态码（0代表没有错误）
         */
        public int read(InputStream is) {

            init();

            if (is != null) {

                in = is;

                readHeader();

                if (!err()) {

                    readContents();

                    if (frameCount < 0) {

                        status = STATUS_FORMAT_ERROR;

                    }

                }

            } else {

                status = STATUS_OPEN_ERROR;

            }

            try {

                is.close();

            } catch (Exception e) {

            }

            return status;

        }

        /**
         * Decodes LZW image data into pixel array. Adapted from John Cristy's
         * BitmapMagick.
         */

        protected void decodeBitmapData() {

            int nullCode = -1;

            int npix = iw * ih;

            int available, clear, code_mask, code_size, end_of_information, in_code, old_code, bits, code, count, i, datum, data_size, first, top, bi, pi;

            if ((pixels == null) || (pixels.length < npix)) {

                pixels = new byte[npix]; // allocate new pixel array

            }

            if (prefix == null) {

                prefix = new short[MAX_STACK_SIZE];

            }

            if (suffix == null) {

                suffix = new byte[MAX_STACK_SIZE];

            }

            if (pixelStack == null) {

                pixelStack = new byte[MAX_STACK_SIZE + 1];

            }

            // Initialize GIF data stream decoder.

            data_size = read();

            clear = 1 << data_size;

            end_of_information = clear + 1;

            available = clear + 2;

            old_code = nullCode;

            code_size = data_size + 1;

            code_mask = (1 << code_size) - 1;

            for (code = 0; code < clear; code++) {

                prefix[code] = 0; // XXX ArrayIndexOutOfBoundsException

                suffix[code] = (byte) code;

            }

            // Decode GIF pixel stream.

            datum = bits = count = first = top = pi = bi = 0;

            for (i = 0; i < npix; ) {

                if (top == 0) {

                    if (bits < code_size) {

                        // Load bytes until there are enough bits for a code.

                        if (count == 0) {

                            // Read a new data block.

                            count = readBlock();

                            if (count <= 0) {

                                break;

                            }

                            bi = 0;

                        }

                        datum += (((int) block[bi]) & 0xff) << bits;

                        bits += 8;

                        bi++;

                        count--;

                        continue;

                    }

                    // Get the next code.

                    code = datum & code_mask;

                    datum >>= code_size;

                    bits -= code_size;

                    // Interpret the code

                    if ((code > available) || (code == end_of_information)) {

                        break;

                    }

                    if (code == clear) {

                        // Reset decoder.

                        code_size = data_size + 1;

                        code_mask = (1 << code_size) - 1;

                        available = clear + 2;

                        old_code = nullCode;

                        continue;

                    }

                    if (old_code == nullCode) {

                        pixelStack[top++] = suffix[code];

                        old_code = code;

                        first = code;

                        continue;

                    }

                    in_code = code;

                    if (code == available) {

                        pixelStack[top++] = (byte) first;

                        code = old_code;

                    }

                    while (code > clear) {

                        pixelStack[top++] = suffix[code];

                        code = prefix[code];

                    }

                    first = ((int) suffix[code]) & 0xff;

                    // Add a new string to the string table,

                    if (available >= MAX_STACK_SIZE) {

                        break;

                    }

                    pixelStack[top++] = (byte) first;

                    prefix[available] = (short) old_code;

                    suffix[available] = (byte) first;

                    available++;

                    if (((available & code_mask) == 0) && (available < MAX_STACK_SIZE)) {

                        code_size++;

                        code_mask += available;

                    }

                    old_code = in_code;

                }

                // Pop a pixel off the pixel stack.

                top--;

                pixels[pi++] = pixelStack[top];

                i++;

            }

            for (i = pi; i < npix; i++) {

                pixels[i] = 0; // clear missing pixels

            }

        }

        /**
         * 如果在读/解码过程中遇到错误，返回true
         */

        protected boolean err() {
            return status != STATUS_OK;
        }

        /**
         * 初始化或重新初始化
         */
        protected void init() {
            status = STATUS_OK;
            frameCount = 0;
            frames = new Vector<GifFrame>();
            gct = null;
            lct = null;
        }

        /**
         * 从输入流中读取一个单字节。
         */
        protected int read() {
            int curByte = 0;
            try {
                curByte = in.read();
            } catch (Exception e) {
                status = STATUS_FORMAT_ERROR;
            }
            return curByte;
        }

        /**
         * Reads next variable length block from input.
         *
         * @return number of bytes stored in "buffer"
         */

        protected int readBlock() {

            blockSize = read();

            int n = 0;

            if (blockSize > 0) {

                try {

                    int count = 0;

                    while (n < blockSize) {

                        count = in.read(block, n, blockSize - n);

                        if (count == -1) {

                            break;

                        }

                        n += count;

                    }

                } catch (Exception e) {

                    e.printStackTrace();

                }

                if (n < blockSize) {

                    status = STATUS_FORMAT_ERROR;

                }

            }

            return n;

        }

        /**
         * Reads color table as 256 RGB integer values
         *
         * @param ncolors int number of colors to read
         * @return int array containing 256 colors (packed ARGB with full alpha)
         */

        protected int[] readColorTable(int ncolors) {

            int nbytes = 3 * ncolors;

            int[] tab = null;

            byte[] c = new byte[nbytes];

            int n = 0;

            try {

                n = in.read(c);

            } catch (Exception e) {

                e.printStackTrace();

            }

            if (n < nbytes) {

                status = STATUS_FORMAT_ERROR;

            } else {

                tab = new int[256]; // max size to avoid bounds checks

                int i = 0;

                int j = 0;

                while (i < ncolors) {

                    int r = ((int) c[j++]) & 0xff;

                    int g = ((int) c[j++]) & 0xff;

                    int b = ((int) c[j++]) & 0xff;

                    tab[i++] = 0xff000000 | (r << 16) | (g << 8) | b;

                }

            }

            return tab;

        }

        /**
         * Main file parser. Reads GIF content blocks.
         */

        protected void readContents() {

            // read GIF file content blocks

            boolean done = false;

            while (!(done || err())) {

                int code = read();

                switch (code) {

                    case 0x2C: // image separator

                        readBitmap();

                        break;

                    case 0x21: // extension

                        code = read();

                        switch (code) {

                            case 0xf9: // graphics control extension

                                readGraphicControlExt();

                                break;

                            case 0xff: // application extension

                                readBlock();

                                String app = "";

                                for (int i = 0; i < 11; i++) {

                                    app += (char) block[i];

                                }

                                if (app.equals("NETSCAPE2.0")) {

                                    readNetscapeExt();

                                } else {

                                    skip(); // don't care

                                }

                                break;

                            case 0xfe:// comment extension

                                skip();

                                break;

                            case 0x01:// plain text extension

                                skip();

                                break;

                            default: // uninteresting extension

                                skip();

                        }

                        break;

                    case 0x3b: // terminator

                        done = true;

                        break;

                    case 0x00: // bad byte, but keep going and see what happens break;

                    default:

                        status = STATUS_FORMAT_ERROR;

                }

            }

        }

        /**
         * Reads Graphics Control Extension values
         */

        protected void readGraphicControlExt() {

            read(); // block size

            int packed = read(); // packed fields

            dispose = (packed & 0x1c) >> 2; // disposal method

            if (dispose == 0) {

                dispose = 1; // elect to keep old image if discretionary

            }

            transparency = (packed & 1) != 0;

            delay = readShort() * 10; // delay in milliseconds

            transIndex = read(); // transparent color index

            read(); // block terminator

        }

        /**
         * 读取gif文件头信息
         */

        protected void readHeader() {

            String id = "";
            for (int i = 0; i < 6; i++) {
                id += (char) read();
            }

            if (!id.startsWith("GIF")) {
                status = STATUS_FORMAT_ERROR;
                return;
            }

            readLSD();
            if (gctFlag && !err()) {
                gct = readColorTable(gctSize);
                bgColor = gct[bgIndex];
            }
        }

        /**
         * 读取下一帧图像
         */
        protected void readBitmap() {

            ix = readShort(); // (sub)image position & size
            iy = readShort();

            iw = readShort();
            ih = readShort();

            int packed = read();

            lctFlag = (packed & 0x80) != 0; // 1 - local color table flag interlace

            lctSize = (int) Math.pow(2, (packed & 0x07) + 1);

            // 3 - sort flag

            // 4-5 - reserved lctSize = 2 << (packed & 7); // 6-8 - local color

            // table size

            interlace = (packed & 0x40) != 0;

            if (lctFlag) {

                lct = readColorTable(lctSize); // read table

                act = lct; // make local table active

            } else {

                act = gct; // make global table active

                if (bgIndex == transIndex) {

                    bgColor = 0;

                }

            }

            int save = 0;

            if (transparency) {

                save = act[transIndex];

                act[transIndex] = 0; // set transparent color if specified

            }

            if (act == null) {

                status = STATUS_FORMAT_ERROR; // no color table defined

            }

            if (err()) {

                return;

            }

            decodeBitmapData(); // decode pixel data

            skip();

            if (err()) {

                return;

            }

            frameCount++;

            // create new image to receive frame data

            image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);

            setPixels(); // transfer pixel data to image

            frames.addElement(new GifFrame(image, delay)); // add image to frame

            // list
            if (transparency) {
                act[transIndex] = save;
            }
            resetFrame();
        }

        /**
         * 读取逻辑屏幕描述符
         */
        protected void readLSD() {

            //逻辑屏幕尺寸
            width = readShort();
            height = readShort();

            // 读取打包字段
            int packed = read();

            gctFlag = (packed & 0x80) != 0; // 1 : 全局色表标志
            // 2-4 : 颜色分辨率
            // 5 : gct排序标志

            gctSize = 2 << (packed & 7); // 6-8 : gct size

            bgIndex = read(); // background color index

            pixelAspect = read(); // pixel aspect ratio

        }

        /**
         * Reads Netscape extenstion to obtain iteration count
         */

        protected void readNetscapeExt() {

            do {

                readBlock();

                if (block[0] == 1) {

                    // loop count sub-block

                    int b1 = ((int) block[1]) & 0xff;

                    int b2 = ((int) block[2]) & 0xff;

                    loopCount = (b2 << 8) | b1;

                }

            } while ((blockSize > 0) && !err());

        }

        /**
         * Reads next 16-bit value, LSB first
         */

        protected int readShort() {

            // read 16-bit value, LSB first

            return read() | (read() << 8);

        }

        /**
         * 复位状态下图像阅读框架。
         */

        protected void resetFrame() {

            lastDispose = dispose;

            lrx = ix;
            lry = iy;
            lrw = iw;
            lrh = ih;

            lastBitmap = image;
            lastBgColor = bgColor;
            dispose = 0;
            transparency = false;
            delay = 0;
            lct = null;
        }

        /**
         * 不可变长度的块跳转到下一个零长度的块
         */
        protected void skip() {
            do {
                readBlock();
            } while ((blockSize > 0) && !err());
        }

    }

}