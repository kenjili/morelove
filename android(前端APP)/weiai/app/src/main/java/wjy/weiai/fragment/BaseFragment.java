package wjy.weiai.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.ButterKnife;
import wjy.weiai.App;
import wjy.weiai.base.BaseActivity;
import wjy.weiai.base.NetworkManager;
import wjy.weiai.request.DaggerLoadDataContractComponent;
import wjy.weiai.request.LoadDataContract;
import wjy.weiai.request.LoadDataContractModules;

/**
 * Fragment基类
 * 
 * @author wjy
 * 
 */
public abstract class BaseFragment extends Fragment
		implements NetworkManager.OnNetWorkChangeLister,LoadDataContractModules.IContent {

	protected View rootView;
	protected LayoutInflater mLayoutInflater;

	@Inject
	LoadDataContract loadDataContract;

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		DaggerLoadDataContractComponent
				.builder()
				.loadDataContractModules(new LoadDataContractModules(this::getContent))
				.build()
				.injection(this);
	}

	/**
	 * FragmentTabHost切换Fragment时避免UI重新加载
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mLayoutInflater = inflater;
		if (this.rootView == null) {
			this.rootView = mLayoutInflater.inflate(getLayoutId(), null);
			ButterKnife.bind(this,this.rootView);
		}
		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return this.rootView;
	}

	/**
	 * 在该方法中查找控件，写入事件等初始化工作
	 */
	protected abstract int getLayoutId();

	/**
	 * onActivityCreated在onCreateView后被调用
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//设置网络状态监听
		((App)getActivity().getApplication()).getNetworkManager().setOnNetWorkChangeLister(this);
		this.onFragmentInitFinish();
	}

	/**
	 * 在该方法中调用网络加载数据
	 */
	protected abstract void onFragmentInitFinish();


	/*******************************************************
	 *      网络状态改变时的处理
	 *******************************************************/

	@Override
	public void onWifi() {

	}

	@Override
	public void onMobile() {

	}

	@Override
	public void onNot() {
		if(getActivity() instanceof  BaseActivity) {
			((BaseActivity) getActivity()).showThreadToast("网络异常，请检查网络连接！");
		}
	}

	/*******************************************************
	 *      end 网络状态改变时的处理
	 *******************************************************/

	@Override
	public Context getContent() {
		return this.getActivity();
	}
}
