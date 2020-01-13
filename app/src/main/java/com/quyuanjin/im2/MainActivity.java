package com.quyuanjin.im2;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.cocosw.bottomsheet.BottomSheet;
import com.gyf.barlibrary.ImmersionBar;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.quyuanjin.im2.ac.AddFriendAc;
import com.quyuanjin.im2.app.App;
import com.quyuanjin.im2.camera.CameraView;
import com.quyuanjin.im2.fragment.ContractFragment;
import com.quyuanjin.im2.fragment.MeFragment;
import com.quyuanjin.im2.fragment.MessageFragment;
import com.quyuanjin.im2.greendao.pojo.Message;
import com.quyuanjin.im2.greendao.pojo.UnReadMessage;
import com.quyuanjin.im2.helputils.OkGoUpdateHttpUtil;
import com.quyuanjin.im2.map.MapMainAc;
import com.quyuanjin.im2.netty.NettyLongChannel;
import com.quyuanjin.im2.netty.helper.SharedPreferencesUtils;
import com.quyuanjin.im2.netty.pojo.SendMessage;
import com.quyuanjin.im2.netty.pojo.StorePullUnReadMsg;
import com.quyuanjin.im2.test.HorizontalNtbActivity;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.UpdateCallback;
import com.vector.update_app.utils.ColorUtil;
import com.wildma.pictureselector.PictureSelector;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import devlight.io.library.ntb.NavigationTabBar;

import static com.quyuanjin.im2.ac.SplashAc.getLocalVersionName;


public class MainActivity extends AppCompatActivity {
    String seleucid ;

    MessageFragment messageFragment = new MessageFragment();
    ContractFragment contractFragment = new ContractFragment();

    MeFragment meFragment = new MeFragment();
    QMUITopBar qmuitopbar;
    ImageView profile;
    SlidingMenu menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //  NettyLongChannel.initNetty();
        EventBus.getDefault().register(this);
        seleucid=getIntent().getStringExtra("selfuuid");
        //设置侧滑菜单栏
        slidingMenu();
        //检查是否是第一次登录
        checkIsFirstLogin();

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CameraView.class);
                startActivity(intent);
            }
        });


        qmuitopbar = findViewById(R.id.qmuitopbar);

        profile = findViewById(R.id.profile_img);


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menu.toggle();

            }
        });

        //    NettyLongChannel.sendAndReflash(ProtoConstant., UUIDHelper.generateUUID());
        //    BottomNavigationView navView = findViewById(R.id.nav_view);
        this.getSupportFragmentManager().beginTransaction().add(R.id.main_fragment_container, messageFragment).commit();
        this.getSupportFragmentManager().beginTransaction().add(R.id.main_fragment_container, contractFragment).commit();
        this.getSupportFragmentManager().beginTransaction().add(R.id.main_fragment_container, meFragment).commit();

        //   navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        checkVersion();

        initUI();
        initBoom();

        initUnMessage();


        changeStatueBar();

    }

    private void changeStatueBar() {
        ImmersionBar.with(this).barColor(R.color.white).
                /*  transparentStatusBar()  //透明状态栏，不写默认透明色
                       .transparentNavigationBar()  //透明导航栏，不写默认黑色(设置此方法，fullScreen()方法自动为true)
                       .transparentBar()             //透明状态栏和导航栏，不写默认状态栏为透明色，导航栏为黑色（设置此方法，fullScreen()方法自动为true）
                       .statusBarColor(R.color.white)     //状态栏颜色，不写默认透明色
                       .navigationBarColor(R.color.white) //导航栏颜色，不写默认黑色
                       .barColor(R.color.white)  //同时自定义状态栏和导航栏颜色，不写默认状态栏为透明色，导航栏为黑色
                       .statusBarAlpha(0.3f)  //状态栏透明度，不写默认0.0f
                       .navigationBarAlpha(0.4f)  //导航栏透明度，不写默认0.0F
                       .barAlpha(0.3f)  //状态栏和导航栏透明度，不写默认0.0f
                       .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                       .flymeOSStatusBarFontColor(R.color.btn3)  //修改flyme OS状态栏字体颜色
                       .fullScreen(true)      //有导航栏的情况下，activity全屏显示，也就是activity最下面被导航栏覆盖，不写默认非全屏
                       .hideBar(BarHide.FLAG_HIDE_BAR)  //隐藏状态栏或导航栏或两者，不写默认不隐藏
                       .addViewSupportTransformColor(toolbar)  //设置支持view变色，可以添加多个view，不指定颜色，默认和状态栏同色，还有两个重载方法
                       .titleBar(view)    //解决状态栏和布局重叠问题，任选其一
                       .titleBarMarginTop(view)     //解决状态栏和布局重叠问题，任选其一
                       .statusBarView(view)  //解决状态栏和布局重叠问题，任选其一
                       .fitsSystemWindows(true)    //解决状态栏和布局重叠问题，任选其一，默认为false，当为true时一定要指定statusBarColor()，不然状态栏为透明色，还有一些重载方法
                       .supportActionBar(true) //支持ActionBar使用
                       .statusBarColorTransform(R.color.orange)  //状态栏变色后的颜色
                       .navigationBarColorTransform(R.color.orange) //导航栏变色后的颜色
                       .barColorTransform(R.color.orange)  //状态栏和导航栏变色后的颜色
                       .removeSupportView(toolbar)  //移除指定view支持
                       .removeSupportAllView() //移除全部view支持
                       .navigationBarEnable(true)   //是否可以修改导航栏颜色，默认为true
                       .navigationBarWithKitkatEnable(true)  //是否可以修改安卓4.4和emui3.1手机导航栏颜色，默认为true
                       .addTag("tag")  //给以上设置的参数打标记
                       .getTag("tag")  //根据tag获得沉浸式参数
                       .reset()  //重置所以沉浸式参数
                       .keyboardEnable(true)  //解决软键盘与底部输入框冲突问题，默认为false，还有一个重载方法，可以指定软键盘mode
                       .keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)  //单独指定软键盘模式
                       .setOnKeyboardListener(new OnKeyboardListener() {    //软键盘监听回调
                           @Override
                           public void onKeyboardChange(boolean isPopup, int keyboardHeight) {
                              //isPopup为true，软键盘弹出，为false，软键盘关闭
                           }
                       }).  */

                        init();
    }


    private void checkIsFirstLogin() {
        String isfirstlogin = (String) SharedPreferencesUtils.getParam(this, "isfirstlogin", "");

        if (isfirstlogin.equals("")) {//isfirstlogin没有赋值，证明是第一次登录，并且给它赋值，本if
            //第二次打开时不再执行
            SharedPreferencesUtils.setParam(this, "isfirstlogin", "ok");

            //首次登录向服务器要已添加的好友名单
            requestFriendListFromServer();

        }
    }

    private void requestFriendListFromServer() {
        String selfuuid = (String) SharedPreferencesUtils.getParam(this, "uuid", "");

        NettyLongChannel.requestFriendList(selfuuid);
    }

    private void initBottomSheet() {
        new BottomSheet.Builder(MainActivity.this).title("title").sheet(R.menu.bottom_sheet).listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case R.id.help:
                        break;
                }
            }
        }).show();

    }

    private void initBoom() {
        BoomMenuButton bmb = findViewById(R.id.bmb);
        //   bmb.setButtonEnum(ButtonEnum.TextInsideCircle);
        //   bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_3_1);
        //    bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_3_3);
    /*    for (int i = 0; i < bmb.getButtonPlaceEnum().buttonNumber(); i++) {
            bmb.addBuilder(new SimpleCircleButton.Builder().buttonCornerRadius(10)
                    .normalImageRes(R.drawable.bat));
        }*/

      /*  for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            TextInsideCircleButton.Builder builder = new TextInsideCircleButton.Builder()
                    .normalImageRes(R.drawable.bat)
                    .normalText("Butter Doesn't fly!");
            bmb.addBuilder(builder);
        }*/
      /*  for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            TextInsideCircleButton.Builder builder = new TextInsideCircleButton.Builder().normalImageRes(R.drawable.bat)
                    .normalText("Butter Doesn't fly!")
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            // When the boom-button corresponding this builder is clicked.
                            Toast.makeText(MainActivity.this, "Clicked " + index, Toast.LENGTH_SHORT).show();
                        switch (index){
                            case 0:
                                Intent intent = new Intent(MainActivity.this, AddFriendAc.class);
                                startActivity(intent);
                                break;
                            case 1:
                                break;

                        }



                        }
                    });  bmb.addBuilder(builder);
       }*/

        assert bmb != null;
        bmb.setButtonEnum(ButtonEnum.Ham);
        //    for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++)
        //      bmb.addBuilder(BuilderManager.getHamButtonBuilderWithDifferentPieceColor());


        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            HamButton.Builder builder = new HamButton.Builder().listener(new OnBMClickListener() {
                @Override
                public void onBoomButtonClick(int index) {
                    Toast.makeText(MainActivity.this, "Clicked " + index, Toast.LENGTH_SHORT).show();
                    switch (index) {
                        case 0:
                            Intent intent = new Intent(MainActivity.this, AddFriendAc.class);
                            startActivity(intent);
                            break;
                        case 1:
                            initBottomSheet();
                            break;
                        case 2:
                            Intent intent2 = new Intent(MainActivity.this, MapMainAc.class);
                            startActivity(intent2);
                            break;
                        case 3:
                            /**
                             * create() 方法参数一是上下文，在 activity 中传 activity.this，在 fragment 中传 fragment.this。参数二为请求码，用于结果回调 onActivityResult 中判断
                             * selectPicture() 方法参数分别为 是否裁剪、裁剪后图片的宽(单位 px)、裁剪后图片的高、宽比例、高比例。都不传则默认为裁剪，宽 200，高 200，宽高比例为 1：1。
                             */
                            PictureSelector
                                    .create(MainActivity.this, PictureSelector.SELECT_REQUEST_CODE)
                                    .selectPicture(true, 200, 200, 1, 1);
                            break;
                        default:
                            break;
                    }
                }
            })
                    .normalImageRes(getImageResource())
                    .normalText(getext())
                    .subNormalText(getSubText());
            bmb.addBuilder(builder);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*结果回调*/
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                String picturePath = data.getStringExtra(PictureSelector.PICTURE_PATH);
                SharedPreferencesUtils.setParam(this, "picturePath", picturePath);
                profile.setImageBitmap(BitmapFactory.decodeFile(picturePath));

                /*如果使用 Glide 加载图片，则需要禁止 Glide 从缓存中加载，因为裁剪后保存的图片地址是相同的*/
                /*RequestOptions requestOptions = RequestOptions
                        .circleCropTransform()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true);
                Glide.with(this).load(picturePath).apply(requestOptions).into(mIvImage);*/
            }
        }
    }

    private void showSimpleBottomSheetList() {
        new QMUIBottomSheet.BottomListSheetBuilder(MainActivity.this)
                .addItem("Item 1")
                .addItem("Item 2")
                .addItem("Item 3")
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "Item " + (position + 1), Toast.LENGTH_SHORT).show();
                    }
                })
                .build()
                .show();
    }


    private static int imageResourceIndex = 0;

    static int getImageResource() {
        if (imageResourceIndex >= imageResources.length) imageResourceIndex = 0;
        return imageResources[imageResourceIndex++];
    }

    private static int[] imageResources = new int[]{
            R.drawable.bat,
            R.drawable.bear,
            R.drawable.bee,
            R.drawable.butterfly
    };
    private static int index = 0;

    private static String[] text = new String[]{"加好友", "分享", "拍照", "地图"

    };

    static String getext() {
        if (index >= text.length) index = 0;
        return text[index++];

    }

    private static String[] subtext = new String[]{"11", "222", "333", "666"

    };
    private static int index2 = 0;

    static String getSubText() {
        if (index2 >= subtext.length) index2 = 0;
        return subtext[index2++];

    }

    ArrayList<NavigationTabBar.Model> models;

    private void initUI() {


        Button button = findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HorizontalNtbActivity.class);
                startActivity(intent);
            }
        });
        String s = (String) SharedPreferencesUtils.getParam(this, "picturePath", "");

        if (!s.equals("")) {
            profile.setImageBitmap(BitmapFactory.decodeFile(s));
        }
        final String[] colors = getResources().getStringArray(R.array.default_preview);

        final NavigationTabBar navigationTabBar = findViewById(R.id.ntb_horizontal);
        models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_first),
                        Color.parseColor(colors[1]))
                        .selectedIcon(getResources().getDrawable(R.drawable.ic_sixth))
                        .title("Heart")
                        //    .badgeTitle("NTB")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_second),
                        Color.parseColor(colors[1]))
//                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
                        .title("Cup")
                        //   .badgeTitle("with")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_third),
                        Color.parseColor(colors[1]))
                        .selectedIcon(getResources().getDrawable(R.drawable.ic_seventh))
                        .title("Diploma")

                        //   .badgeTitle("state")
                        .build()
        );


        navigationTabBar.setModels(models);
        navigationTabBar.setModelIndex(0);

        //  navigationTabBar.setTitleMode(NavigationTabBar.TitleMode.ACTIVE);
       /* navigationTabBar.setBadgeGravity(NavigationTabBar.BadgeGravity.BOTTOM);
        navigationTabBar.setBadgePosition(NavigationTabBar.BadgePosition.CENTER);
        navigationTabBar.setTypeface("fonts/custom_font.ttf");
        navigationTabBar.setIsBadged(true);
        navigationTabBar.setIsTitled(true);
        navigationTabBar.setIsTinted(true);
        navigationTabBar.setIsBadgeUseTypeface(true);*/
        //  navigationTabBar.setBadgeBgColor(Color.RED);
        //   navigationTabBar.setBadgeTitleColor(Color.WHITE);
        //  navigationTabBar.setIsSwiped(true);
        navigationTabBar.setBgColor(Color.WHITE);
        //  navigationTabBar.setBadgeSize(10);
        //   navigationTabBar.setTitleSize(10);
        //   navigationTabBar.setIconSizeFraction(0.5f);


        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                // navigationTabBar.getModels().get(position).hideBadge();


            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });
        navigationTabBar.setOnTabBarSelectedIndexListener(new NavigationTabBar.OnTabBarSelectedIndexListener() {
            @Override
            public void onStartTabSelected(NavigationTabBar.Model model, int index) {
                switch (index) {
                    case 0:

                        changeFragment(messageFragment);
                        qmuitopbar.setVisibility(View.VISIBLE);
                        model.hideBadge();
                        break;
                    case 1:
                        changeFragment(contractFragment);
                        qmuitopbar.setVisibility(View.VISIBLE);
                        model.hideBadge();
                        break;
                    case 2:
                        changeFragment(meFragment);
                        qmuitopbar.setVisibility(View.GONE);
                        model.hideBadge();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onEndTabSelected(NavigationTabBar.Model model, int index) {

            }
        });


      /*  navigationTabBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < navigationTabBar.getModels().size(); i++) {
                    final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
                    navigationTabBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            model.showBadge();
                        }
                    }, i * 5);
                }
            }
        }, 10);*/
    }

    void initUnMessage() {
        models.get(0).hideBadge();
        models.get(2).setBadgeTitle("123");

    }

    private void slidingMenu() {

//创建对象
        menu = new SlidingMenu(this);
        //设置侧滑模式
        menu.setMode(SlidingMenu.LEFT);
        //设置点击模式
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setTouchModeBehind(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setBehindOffset(200);//主界面剩余
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //设置侧滑布局

        menu.setMenu(R.layout.ac_left_menu);

    }

    private void changeFragment(Fragment fragment) {
        this.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, fragment).commit();


    }


    private void checkVersion() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();

        String updateURL = "http://www.samuer.top/json";
        Map<String, String> params = new HashMap<String, String>();

        //  params.put("appKey", "ab55ce55Ac4bcP408cPb8c1Aaeac179c5f6f");
        params.put("appVersion", getLocalVersionName(this));
        //    params.put("key1", "value2");
        //   params.put("key2", "value3");

        new UpdateAppManager
                .Builder()
                //必须设置，当前Activity
                .setActivity(this)
                //必须设置，实现httpManager接口的对象
                .setHttpManager(new OkGoUpdateHttpUtil())
                //必须设置，更新地址
                .setUpdateUrl(updateURL)

                //以下设置，都是可选
                //设置请求方式，默认get
                .setPost(false)
                //添加自定义参数，默认version=1.0.0（app的versionName）；apkKey=唯一表示（在AndroidManifest.xml配置）
                .setParams(params)
                //设置点击升级后，消失对话框，默认点击升级后，对话框显示下载进度
                //.hideDialogOnDownloading()
                //设置头部，不设置显示默认的图片，设置图片后自动识别主色调，然后为按钮，进度条设置颜色
                .setTopPic(R.mipmap.top_8)
                //为按钮，进度条设置颜色，默认从顶部图片自动识别。
                .setThemeColor(ColorUtil.getRandomColor())
                //设置apk下砸路径，默认是在下载到sd卡下/Download/1.0.0/test.apk
                .setTargetPath(path)
                //设置appKey，默认从AndroidManifest.xml获取，如果，使用自定义参数，则此项无效
                //.setAppKey("ab55ce55Ac4bcP408cPb8c1Aaeac179c5f6f")
                //不显示通知栏进度条
                //  .dismissNotificationProgress()
                //是否忽略版本
                .showIgnoreVersion()

                .build()
                //检测是否有新版本
                .checkNewApp(new UpdateCallback() {
                    /**
                     * 解析json,自定义协议
                     *
                     * @param json 服务器返回的json
                     * @return UpdateAppBean
                     */
                    @Override
                    protected UpdateAppBean parseJson(String json) {
                        UpdateAppBean updateAppBean = new UpdateAppBean();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            updateAppBean
                                    //（必须）是否更新Yes,No
                                    .setUpdate(jsonObject.optString("update"))
                                    //（必须）新版本号，
                                    .setNewVersion(jsonObject.optString("new_version"))
                                    //（必须）下载地址
                                    .setApkFileUrl(jsonObject.optString("apk_file_url"))
                                    //（必须）更新内容
                                    .setUpdateLog(jsonObject.optString("update_log"))
                                    //大小，不设置不显示大小，可以不设置
                                    .setTargetSize(jsonObject.optString("target_size"))
                                    //是否强制更新，可以不设置
                                    .setConstraint(false);
                            //设置md5，可以不设置
                            // .setNewMd5(jsonObject.optString("new_md51"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return updateAppBean;
                    }

                    /**
                     * 网络请求之前
                     */
                    @Override
                    public void onBefore() {
                        //     CProgressDialogUtils.showProgressDialog(MainActivity.this);
                    }

                    /**
                     * 网路请求之后
                     */
                    @Override
                    public void onAfter() {
                        //     CProgressDialogUtils.cancelProgressDialog(MainActivity.this);
                    }

                    /**
                     * 没有新版本
                     */
                    @Override
                    public void noNewApp(String e) {
                        //   Toast.makeText(MainActivity.this, "没有新版本", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    protected void hasNewApp(UpdateAppBean updateApp, UpdateAppManager updateAppManager) {
                        super.hasNewApp(updateApp, updateAppManager);
                        //自定义对话框
                        //     showDiyDialog(updateApp, updateAppManager);
                    }

                });
    }

    private void showDiyDialog(UpdateAppBean updateApp, final UpdateAppManager updateAppManager) {

        String targetSize = updateApp.getTargetSize();
        String updateLog = updateApp.getUpdateLog();

        String msg = "";

        if (!TextUtils.isEmpty(targetSize)) {
            msg = "新版本大小：" + targetSize + "\n\n";
        }

        if (!TextUtils.isEmpty(updateLog)) {
            msg += updateLog;
        }

        new AlertDialog.Builder(this)
                .setTitle(String.format("是否升级到%s版本？", updateApp.getNewVersion()))
                .setMessage(msg)
                .setPositiveButton("升级", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //不显示下载进度
                        updateAppManager.download();

                        dialog.dismiss();
                    }
                })
                .setNegativeButton("暂不升级", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    //这里是唯一的存储聊天具体信息的方法
    //无论消息阅读没阅读都存进去
    //在这里应该设置阅读状态
    // 然后在chat里面拉取时更改阅读状态

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStoreRecievedMsg(SendMessage sendMessage) {
        String receMsg = sendMessage.getMsg();
        String s = sendMessage.getUuid();
        insertNewMsg(receMsg, s);//0 means Msg.TYPE_RECEIVED
    }

    private void insertNewMsg(String msg, String receiveUUID) {
        String selfuuid = (String) SharedPreferencesUtils.getParam(this, "uuid", "");

        Message message = new Message();
        message.setMsg(msg);
        message.setReceiveId(receiveUUID);
        message.setSendID(selfuuid);
        message.setType("0");
        App.getDaoSession().getMessageDao().insert(message);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStoreRecieved(StorePullUnReadMsg storePullUnReadMsg) {
        String selfuuid = (String) SharedPreferencesUtils.getParam(this, "uuid", "");

        String[] recemsg = storePullUnReadMsg.getMsg();
        for (int i = 1; i < recemsg.length; i += 3) {
            UnReadMessage unReadMessage = new UnReadMessage();
            unReadMessage.setSenderId(recemsg[i]);
            unReadMessage.setSendTime(recemsg[i + 1]);
            unReadMessage.setMsg(recemsg[i + 2]);
            App.getDaoSession().getUnReadMessageDao().insert(unReadMessage);

            //逻辑要清楚 message 发送时，自身是发送者，对方是接收者
            // 接收时 自身是接收者，对方是发送者
            //现在是接收数据，所以自身是接收者，将对方的uuid存进发送者里
            Message message = new Message();
            message.setType("0");
            message.setSendID(recemsg[i]);
            message.setReceiveId(selfuuid);
            message.setCreateTime(recemsg[i + 1]);
            message.setMsg(recemsg[i + 2]);
            App.getDaoSession().getMessageDao().insert(message);

        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        //从服务器拉取未读信息
        //如果已登录过，从服务器拉取未读信息
        try {

            NettyLongChannel.sendPullUnreadMsg(seleucid);
            //还要拉取未添加好友信息
            NettyLongChannel.sendPullUnreadAddFriendMsg(seleucid);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
