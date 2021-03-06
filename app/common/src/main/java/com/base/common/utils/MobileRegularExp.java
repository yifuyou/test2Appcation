package com.base.common.utils;

/**
 * 国际短号	简称		中文国家名称			正则
 * 86		CN		中国大陆			^(86){0,1}1\d{10}$
 * 886		TW		中国台湾地区			^(00){0,1}(886){1}0{0,1}[6,7,9](?:\d{7}|\d{8}|\d{10})$
 * 852		HK		中国香港			^(00){0,1}(852){1}0{0,1}[1,5,6,9](?:\d{7}|\d{8}|\d{12})$
 * 60		MY		马来西亚			^(00){0,1}(60){1}1\d{8,9}$
 * 65		SG		新加坡			^(00){0,1}(65){1}[13689]\d{6,7}$
 * 81		JP		日本			^(00){0,1}(81){1}0{0,1}[7,8,9](?:\d{8}|\d{9})$
 * 82		KR		韩国			^(00){0,1}(82){1}0{0,1}[7,1](?:\d{8}|\d{9})$
 * 1		US		美国			^(00){0,1}(1){1}\d{10,12}$
 * 1		CA		加拿大			^(00){0,1}(1){1}\d{10}$
 * 61		AU		澳大利亚			^(00){0,1}(61){1}4\d{8,9}$
 * 64		NZ		新西兰			^(00){0,1}(64){1}[278]\d{7,9}$
 * 355		AL		阿尔巴尼亚			^(00){0,1}(355){1}\d{6,15}$
 * 244		AO		阿根廷			^(00){0,1}(244){1}\d{6,15}$
 * 54		AR		阿联酋			^(00){0,1}(54){1}\d{6,12}$
 * 43		AT		阿曼			^(00){0,1}(43){1}\d{6,12}$
 * 994		AZ		阿塞拜疆			^(00){0,1}(994){1}\d{6,15}$
 * 1242		BS		爱尔兰			^(00){0,1}(1242){1}\d{6,12}$
 * 973		BH		埃及			^(00){0,1}(973){1}\d{6,15}$
 * 375		BY		爱沙尼亚			^(00){0,1}(375){1}\d{6,12}$
 * 32		BE		安哥拉			^(00){0,1}(32){1}\d{6,12}$
 * 501		BZ		奥地利			^(00){0,1}(501){1}\d{6,12}$
 * 229		BJ		中国澳门			^(00){0,1}(229){1}\d{6,15}$
 * 591		BO		巴布亚新几内亚			^(00){0,1}(591){1}\d{6,15}$
 * 55		BR		巴哈马			^(00){0,1}(55){1}\d{6,12}$
 * 673		BN		巴勒斯坦			^(00){0,1}(673){1}\d{6,15}$
 * 359		BG		巴林			^(00){0,1}(359){1}\d{6,12}$
 * 226		BF		巴拿马			^(00){0,1}(226){1}\d{6,15}$
 * 855		KH		巴西			^(00){0,1}(855){1}\d{6,12}$
 * 237		CM		白俄罗斯			^(00){0,1}(237){1}\d{6,15}$
 * 238		CV		保加利亚			^(00){0,1}(238){1}\d{6,15}$
 * 1345		KY		贝宁			^(00){0,1}(1345){1}\d{6,15}$
 * 235		TD		比利时			^(00){0,1}(235){1}\d{6,15}$
 * 56		CL		秘鲁			^(00){0,1}(56){1}\d{6,12}$
 * 57		CO		波兰			^(00){0,1}(57){1}\d{6,12}$
 * 269		KM		玻利维亚			^(00){0,1}(269){1}\d{6,15}$
 * 506		CR		伯利兹			^(00){0,1}(506){1}\d{6,15}$
 * 385		HR		布基纳法索			^(00){0,1}(385){1}\d{6,15}$
 * 357		CY		赤道几内亚			^(00){0,1}(357){1}\d{6,15}$
 * 45		DK		丹麦			^(00){0,1}(45){1}\d{6,12}$
 * 253		DJ		德国			^(00){0,1}(253){1}\d{6,15}$
 * 20		EG		多哥			^(00){0,1}(20){1}\d{6,12}$
 * 503		SV		俄罗斯			^(00){0,1}(503){1}\d{6,15}$
 * 240		GQ		法国			^(00){0,1}(240){1}\d{6,15}$
 * 372		EE		菲律宾			^(00){0,1}(372){1}\d{6,12}$
 * 358		FI		芬兰			^(00){0,1}(358){1}\d{6,12}$
 * 33		FR		佛得角			^(00){0,1}(33){1}(\d{6}|\d{8,9})$
 * 241		GA		冈比亚			^(00){0,1}(241){1}\d{6,15}$
 * 220		GM		格林纳达			^(00){0,1}(220){1}\d{6,15}$
 * 995		GE		格鲁吉亚			^(00){0,1}(995){1}\d{6,15}$
 * 49		DE		哥伦比亚			^(00){0,1}(49){1}1(\d{5,6}|\d{9,12})$
 * 30		GR		哥斯达黎加			^(00){0,1}(30){1}\d{6,12}$
 * 1473		GD		圭亚那			^(00){0,1}(1473){1}\d{6,15}$
 * 502		GT		荷兰			^(00){0,1}(502){1}\d{6,15}$
 * 224		GN		洪都拉斯			^(00){0,1}(224){1}\d{6,15}$
 * 245		GW		吉布提			^(00){0,1}(245){1}\d{6,15}$
 * 592		GY		吉尔吉斯斯坦			^(00){0,1}(592){1}\d{6,15}$
 * 504		HN		几内亚			^(00){0,1}(504){1}\d{6,15}$
 * 36		HU		几内亚比绍			^(00){0,1}(36){1}\d{6,12}$
 * 91		IN		加蓬			^(00){0,1}(91){1}\d{6,12}$
 * 62		ID		柬埔寨			^(00){0,1}(62){1}[2-9]\d{7,11}$
 * 353		IE		津巴布韦			^(00){0,1}(353){1}\d{6,12}$
 * 972		IL		喀麦隆			^(00){0,1}(972){1}\d{6,12}$
 * 39		IT		卡塔尔			^(00){0,1}(39){1}[37]\d{8,11}$
 * 1876		JM		开曼群岛			^(00){0,1}(1876){1}\d{6,15}$
 * 962		JO		克罗地亚			^(00){0,1}(962){1}\d{6,12}$
 * 254		KE		科摩罗			^(00){0,1}(254){1}\d{6,15}$
 * 965		KW		科威特			^(00){0,1}(965){1}\d{6,15}$
 * 996		KG		肯尼亚			^(00){0,1}(996){1}\d{6,12}$
 * 371		LV		拉脱维亚			^(00){0,1}(371){1}\d{6,15}$
 * 266		LS		莱索托			^(00){0,1}(266){1}\d{6,15}$
 * 370		LT		立陶宛			^(00){0,1}(370){1}\d{6,12}$
 * 352		LU		卢森堡			^(00){0,1}(352){1}\d{6,12}$
 * 853		MO		卢旺达			^(00){0,1}(853){1}6\d{7}$
 * 261		MG		罗马尼亚			^(00){0,1}(261){1}\d{6,15}$
 * 265		MW		马达加斯加			^(00){0,1}(265){1}\d{6,15}$
 * 960		MV		马尔代夫			^(00){0,1}(960){1}\d{6,12}$
 * 223		ML		马拉维			^(00){0,1}(223){1}\d{6,15}$
 * 222		MR		马里			^(00){0,1}(222){1}\d{6,15}$
 * 230		MU		毛里求斯			^(00){0,1}(230){1}\d{6,15}$
 * 52		MX		毛里塔尼亚			^(00){0,1}(52){1}\d{6,12}$
 * 373		MD		蒙古			^(00){0,1}(373){1}\d{6,15}$
 * 976		MN		摩尔多瓦			^(00){0,1}(976){1}\d{6,12}$
 * 212		MA		摩洛哥			^(00){0,1}(212){1}\d{6,12}$
 * 258		MZ		莫桑比克			^(00){0,1}(258){1}\d{6,15}$
 * 264		NA		墨西哥			^(00){0,1}(264){1}\d{6,15}$
 * 31		NL		纳米比亚			^(00){0,1}(31){1}6\d{8}$
 * 505		NI		南非			^(00){0,1}(505){1}\d{6,15}$
 * 227		NE		尼加拉瓜			^(00){0,1}(227){1}\d{6,15}$
 * 234		NG		尼日尔			^(00){0,1}(234){1}\d{6,12}$
 * 47		NO		尼日利亚			^(00){0,1}(47){1}\d{6,12}$
 * 968		OM		挪威			^(00){0,1}(968){1}\d{6,15}$
 * 970		PS		葡萄牙			^(00){0,1}(970){1}\d{6,15}$
 * 507		PA		瑞典			^(00){0,1}(507){1}\d{6,12}$
 * 675		PG		瑞士			^(00){0,1}(675){1}\d{6,15}$
 * 51		PE		萨尔瓦多			^(00){0,1}(51){1}\d{6,12}$
 * 63		PH		塞尔维亚			^(00){0,1}(63){1}[24579](\d{7,9}|\d{12})$
 * 48		PL		塞拉利昂			^(00){0,1}(48){1}\d{6,12}$
 * 351		PT		塞内加尔			^(00){0,1}(351){1}\d{6,12}$
 * 974		QA		塞浦路斯			^(00){0,1}(974){1}\d{6,12}$
 * 40		RO		塞舌尔			^(00){0,1}(40){1}\d{6,12}$
 * 7		RU		沙特阿拉伯			^(00){0,1}(7){1}[13489]\d{9,11}$
 * 250		RW		斯里兰卡			^(00){0,1}(250){1}\d{6,15}$
 * 966		SA		斯洛伐克			^(00){0,1}(966){1}\d{6,12}$
 * 221		SN		斯洛文尼亚			^(00){0,1}(221){1}\d{6,15}$
 * 381		RS		斯威士兰			^(00){0,1}(381){1}\d{6,12}$
 * 248		SC		苏里南			^(00){0,1}(248){1}\d{6,12}$
 * 232		SL		塔吉克斯坦			^(00){0,1}(232){1}\d{6,15}$
 * 421		SK		泰国			^(00){0,1}(421){1}\d{6,15}$
 * 386		SI		坦桑尼亚			^(00){0,1}(386){1}\d{6,15}$
 * 27		ZA		特立尼达和多巴哥			^(00){0,1}(27){1}\d{6,12}$
 * 34		ES		土耳其			^(00){0,1}(34){1}\d{6,12}$
 * 94		LK		土库曼斯坦			^(00){0,1}(94){1}\d{6,12}$
 * 597		SR		突尼斯			^(00){0,1}(597){1}\d{6,15}$
 * 268		SZ		危地马拉			^(00){0,1}(268){1}\d{6,15}$
 * 46		SE		委内瑞拉			^(00){0,1}(46){1}[124-7](\d{8}|\d{10}|\d{12})$
 * 41		CH		文莱			^(00){0,1}(41){1}\d{6,12}$
 * 992		TJ		乌干达			^(00){0,1}(992){1}\d{6,15}$
 * 255		TZ		乌克兰			^(00){0,1}(255){1}\d{6,15}$
 * 66		TH		乌拉圭			^(00){0,1}(66){1}[13456789]\d{7,8}$
 * 228		TG		乌兹别克斯坦			^(00){0,1}(228){1}\d{6,15}$
 * 1868		TT		西班牙			^(00){0,1}(1868){1}\d{6,15}$
 * 216		TN		希腊			^(00){0,1}(216){1}\d{6,12}$
 * 90		TR		匈牙利			^(00){0,1}(90){1}\d{6,12}$
 * 993		TM		牙买加			^(00){0,1}(993){1}\d{6,15}$
 * 256		UG		也门			^(00){0,1}(256){1}\d{6,15}$
 * 380		UA		意大利			^(00){0,1}(380){1}[3-79]\d{8,9}$
 * 971		AE		以色列			^(00){0,1}(971){1}\d{6,12}$
 * 44		GB		印度			^(00){0,1}(44){1}[347-9](\d{8,9}|\d{11,12})$
 * 598		UY		印度尼西亚			^(00){0,1}(598){1}\d{6,15}$
 * 998		UZ		英国			^(00){0,1}(998){1}\d{6,15}$
 * 58		VE		英属维尔京群岛			^(00){0,1}(58){1}\d{6,12}$
 * 84		VN		约旦			^(00){0,1}(84){1}[1-9]\d{6,9}$
 * 1284		VG		越南			^(00){0,1}(1284){1}\d{6,12}$
 * 967		YE		赞比亚			^(00){0,1}(967){1}\d{6,15}$
 * 260		ZM		乍得			^(00){0,1}(260){1}\d{6,15}$
 * 263		ZW		智利			^(00){0,1}(263){1}\d{6,15}$
 * <p>
 * <p>
 * 国际手机号码  验证正则
 */
public enum MobileRegularExp {

    CN("中国", "^(\\+?0?86\\-?)?1[345789]\\d{9}$"),
    TW("台湾", "^(\\+?886\\-?|0)?9\\d{8}$"),
    HK("香港", "^(\\+?852\\-?)?[569]\\d{3}\\-?\\d{4}$"),
    MY("马来西亚", "^(\\+?6?01){1}(([145]{1}(\\-|\\s)?\\d{7,8})|([236789]{1}(\\s|\\-)?\\d{7}))$"),
    PH("菲律宾", "^(\\+?0?63\\-?)?\\d{10}$"),
    TH("泰国", "^(\\+?0?66\\-?)?\\d{10}$"),
    SG("新加坡", "^(\\+?0?65\\-?)?\\d{10}$"),
    /* 以上是项目可能设计到的市场，一下是其他国家的手机号校验正则，欢迎补充*/
    DZ("阿尔及利亚", "^(\\+?213|0)(5|6|7)\\d{8}$"),
    SY("叙利亚", "^(!?(\\+?963)|0)?9\\d{8}$"),
    SA("沙特阿拉伯", "^(!?(\\+?966)|0)?5\\d{8}$"),
    US("美国", "^(\\+?1)?[2-9]\\d{2}[2-9](?!11)\\d{6}$"),
    CZ("捷克共和国", "^(\\+?420)? ?[1-9][0-9]{2} ?[0-9]{3} ?[0-9]{3}$"),
    DE("德国", "^(\\+?49[ \\.\\-])?([\\(]{1}[0-9]{1,6}[\\)])?([0-9 \\.\\-\\/]{3,20})((x|ext|extension)[ ]?[0-9]{1,4})?$"),
    DK("丹麦", "^(\\+?45)?(\\d{8})$"),
    GR("希腊", "^(\\+?30)?(69\\d{8})$"),
    AU("澳大利亚", "^(\\+?61|0)4\\d{8}$"),
    GB("英国", "^(\\+?44|0)7\\d{9}$"),
    CA("加拿大", "^(\\+?1)?[2-9]\\d{2}[2-9](?!11)\\d{6}$"),
    IN("印度", "^(\\+?91|0)?[789]\\d{9}$"),
    NZ("新西兰", "^(\\+?64|0)2\\d{7,9}$"),
    ZA("南非", "^(\\+?27|0)\\d{9}$"),
    ZM("赞比亚", "^(\\+?26)?09[567]\\d{7}$"),
    ES("西班牙", "^(\\+?34)?(6\\d{1}|7[1234])\\d{7}$"),
    FI("芬兰", "^(\\+?358|0)\\s?(4(0|1|2|4|5)?|50)\\s?(\\d\\s?){4,8}\\d$"),
    FR("法国", "^(\\+?33|0)[67]\\d{8}$"),
    IL("以色列", "^(\\+972|0)([23489]|5[0248]|77)[1-9]\\d{6}"),
    HU("匈牙利", "^(\\+?36)(20|30|70)\\d{7}$"),
    IT("意大利", "^(\\+?39)?\\s?3\\d{2} ?\\d{6,7}$"),
    JP("日本", "^(\\+?81|0)\\d{1,4}[ \\-]?\\d{1,4}[ \\-]?\\d{4}$"),
    NO("挪威", "^(\\+?47)?[49]\\d{7}$"),
    BE("比利时", "^(\\+?32|0)4?\\d{8}$"),
    PL("波兰", "^(\\+?48)? ?[5-8]\\d ?\\d{3} ?\\d{2} ?\\d{2}$"),
    BR("巴西", "^(\\+?55|0)\\-?[1-9]{2}\\-?[2-9]{1}\\d{3,4}\\-?\\d{4}$"),
    PT("葡萄牙", "^(\\+?351)?9[1236]\\d{7}$"),
    RU("俄罗斯", "^(\\+?7|8)?9\\d{9}$"),
    RS("塞尔维亚", "^(\\+3816|06)[- \\d]{5,9}$"),
    R("土耳其", "^(\\+?90|0)?5\\d{9}$"),
    VN("越南", "^(\\+?84|0)?((1(2([0-9])|6([2-9])|88|99))|(9((?!5)[0-9])))([0-9]{7})$");

    /**
     * 国际名称
     */
    private String national;

    /**
     * 正则表达式
     */
    private String regularExp;

    public String getNational() {
        return national;
    }

    public void setNational(String national) {
        this.national = national;
    }

    public String getRegularExp() {
        return regularExp;
    }

    public void setRegularExp(String regularExp) {
        this.regularExp = regularExp;
    }

    MobileRegularExp(String national, String regularExp) {
        this.national = national;
        this.regularExp = regularExp;
    }

}
