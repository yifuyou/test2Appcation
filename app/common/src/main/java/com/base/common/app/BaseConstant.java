package com.base.common.app;


import com.base.common.utils.IOUtils;
import com.base.common.utils.UIUtils;


/**
 * 十	十六	控制	转义	说明
 * 进制	进制	字符	字符 *
 * 0	0	NUL	/0	Null character( 空字符 )
 * 1	1	SOH		Start of HeaderBean( 标题开始 )
 * 2	2	STX		Start of Text( 正文开始 )
 * 3	3	ETX		End of Text( 正文结束 )
 * 4	4	EOT		End of Transmission( 传输结束 )
 * 5	5	ENQ		Enquiry( 请求 )
 * 6	6	ACK		Acknowledgment( 收到通知 / 响应 )
 * 7	7	BEL	/a	Bell ( 响铃 )
 * 8	8	BS	/b	Backspace( 退格 )
 * 9	9	HT	/t	Horizontal Tab( 水平制表符 )
 * 10	0A	LF	/n	Line feed( 换行键 )
 * 11	0B	VT	/v	Vertical Tab( 垂直制表符 )
 * 12	0C	FF	/f	Form feed( 换页键 )
 * 13	0D	CR	/r	Carriage return( 回车键 )
 * 14	0E	SO		Shift Out( 不用切换 )
 * 15	0F	SI		Shift In( 启用切换 )
 * 16	10	DLE		Data Link Escape( 数据链路转义 )
 * 17	11	DC1		Device Control 1( 设备控制 1) /XON(Transmit On)
 * 18	12	DC2		Device Control 2( 设备控制 2)
 * 19	13	DC3		Device Control 3( 设备控制 3) /XOFF(Transmit Off)
 * 20	14	DC4		Device Control 4( 设备控制 4)
 * 21	15	NAK		Negative Acknowledgement( 拒绝接收 / 无响应 )
 * 22	16	SYN		Synchronous Idle( 同步空闲 )
 * 23	17	ETB		End of Trans the Block( 传输块结束 )
 * 24	18	CAN		Cancel( 取消 )
 * 25	19	EM		End of Medium( 已到介质末端 / 介质存储已满 )
 * 26	1A	SUB		Substitute( 替补 / 替换 )
 * 27	1B	ESC	/e	Escape( 溢出 / 逃离 / 取消 )
 * <p>
 * 28	1C	FS		File Separator( 文件分割符 )
 * 29	1D	GS		ic_right_icon Separator( 分组符 )
 * 30	1E	RS		Record Separator( 记录分隔符 )
 * 31	1F	US		Unit Separator( 单元分隔符 )
 * <p>
 * 0-31的在打印时是不显示的
 */

public interface BaseConstant {

    //银行卡验证地址
    String bank_discern_url = "https://ccdcapi.alipay.com/validateAndCacheCardInfo.json";
    //https://apimg.alipay.com/combo.png?d=cashier&t=CCB  银行卡图标
    String bank_icon_url = "https://apimg.alipay.com/combo.png?d=cashier&t=";


    String USER_BEAN = "user_bean"; //登录信息
    String GATEWAY_TOKEN = "gateway_token";  //登录的token
    String GATEWAY_USERID = " gateway_userid";//角色id

    String EXPIRE_TIME = "expire_time";  // 过期时间
    String INVALID_TIME = "invalid_time";  //失效时间

    String LOGIN_IN = "LOGIN";  //需要用户登录  ps:处理后台接口返回时，需要登录的状态
    String NetworkEvent = "NetworkEvent";//网络连接
    String NetworkEventChecked = "NetworkEventChecked";//网络连接检查

    char FS = 28;//文件分割符   在本文中用于 段落 的分割
    char GS = 29;//分组符       在本文中用于 一行 的分割
    char RS = 30;//记录分隔符   在本文中用于 一列 的分割
    char US = 31;//单元分隔符   在本文中用于 一列中小项 的的分割


    String AUTHORITY = UIUtils.getPackageName() + ".fileProvider";

    // 更新包在sdk中的存储位置
    String APKSaveDir = IOUtils.getSdCardPath() + "/malaysiaStore";
    String APK_NAME = "malaysiaStore.apk";

    // 图片在sdk中的存储位置
    String imageSaveDir = APKSaveDir + "/images";
    // 数据备份位置
    String backupSaveDir = APKSaveDir + "/backup";
    // 日志存放位置
    String logSaveDir = APKSaveDir + "/log";
    //音频文件
    String voice = APKSaveDir + "/voice";
    //礼物存放地址
    String giftSaveDir = APKSaveDir + "/webp";

    String DATABASE_PASSWORD = "cmp1eEtYaWNrTHQyK24wag==";


    interface EventKey {
        //需要用户信息更新事件  收到这个事件会请求用户信息
        String USER_UPDATE = "user_update";

        //用户信息更新成功事件 ,用户信息 请求完成 并缓存在sp后会发出这个事件
        String USER_UPDATE_SUCCESS = "user_update_success";


        String LOGIN_OUT = "login_out";    //用户退出登录
        String LOGIN_SUCC = "login_succ";//登录成功


        String LOGIN_OUT_ONCLICK = "login_out_onclick";    //用户手动退出登录


        String PAY_SUCCESS = "pay_success";//发送支付成功事件，相关页面关闭


    }


}
