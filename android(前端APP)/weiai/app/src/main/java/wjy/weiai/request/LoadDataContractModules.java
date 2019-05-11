package wjy.weiai.request;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class LoadDataContractModules {

    //@Provides组件提供者,dagger2会调用这个方法取得IContent
    @Provides
    public IContent provideContent() {
        return this.iContent;
    }


    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    public interface IContent {
        Context getContent();
    }

    //我们自己d调用的
    private IContent iContent;

    public LoadDataContractModules(IContent iContent) {
        this.iContent = iContent;
    }
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
}
