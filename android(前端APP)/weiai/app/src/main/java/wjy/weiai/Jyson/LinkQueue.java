package wjy.weiai.Jyson;

/**
 * 单向列表队列
 * @author wujiuye
 *
 * @param <T>
 */
public class LinkQueue<T>{
	/**
	 * 节点类
	 * @author wujiuye
	 *
	 */
	public class Node
	{
		T obj;
		Node next;
		
		public Node()
		{
			
		}
		
		public Node(T obj, Node next) {
			super();
			this.obj = obj;
			this.next = next;
		}

		public T getObj() {
			return obj;
		}

		public void setObj(T obj) {
			this.obj = obj;
		}

		public Node getNext() {
			return next;
		}

		public void setNext(Node next) {
			this.next = next;
		}
	}
	
	private Node header=null;
	private Node botton=null;
	
	
	/**
	 * 进队列
	 * @param obj
	 */
	public void enqueue(T obj)
	{
		Node node = new Node();
		node.next=null;
		node.obj=obj;
		if(header==null)
		{
			header=node;
			botton=node;
			return ;
		}
		botton.next=node;
		botton=node;
	}
	
	/**
	 * 出队列
	 * @return
	 */
	public T dnqueue()
	{
		if(header==null)
			return null;
		Node node = header;
		header = node.next;
		return node.obj;
	}
	
	/**
	 * 清空队列
	 */
	public void clean()
	{
		header=null;
		botton=null;
	}

	
	/**
	 * 获取队列的首个对象，不会在队列中移除
	 * @return
	 */
	public T peek()
	{
		return header.obj;
	}
	
	/**
	 * 判断队列是否为空
	 */
	public boolean isNull()
	{
		return header==null;
	}
}
