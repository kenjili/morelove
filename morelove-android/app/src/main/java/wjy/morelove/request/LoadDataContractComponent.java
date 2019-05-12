package wjy.morelove.request;

import dagger.Component;
import wjy.morelove.App;
import wjy.morelove.base.BaseActivity;
import wjy.morelove.fragment.BaseFragment;


@Component(modules = LoadDataContractModules.class)
public interface LoadDataContractComponent {

    /**
     * 为BaseActivity的实例（可以是子类实例）注入依赖，即为@Inject声明的字段
     * 从LoadDataContractModules的实例中获取返回值类型相同的被@Provides声明的方法，
     * 这里先不讨论多个返回值类型相同的被@Provides声明的方法。
     *
     * @param activity
     */
    void injection(BaseActivity activity);

    /**
     * 为App类型的实例对象注入依赖，即通过调用从指定的每个module中的被@Provides声明的方法查找返回值类型相同的方法实现依赖注入
     *
     * @param app
     */
    void injection(App app);

    /**
     * 为BaseFragment类的实例（或子类实例）注入依赖
     *
     * @param fragment
     */
    void injection(BaseFragment fragment);
}
