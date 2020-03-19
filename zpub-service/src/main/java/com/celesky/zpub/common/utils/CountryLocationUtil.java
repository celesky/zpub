package com.celesky.zpub.common.utils;


import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

/**
 * @author:pan
 * @date:2018-08-09
 */
@Slf4j
public class CountryLocationUtil {

    private static Integer[] codes = null;

    private static HashMap<Integer, String> codeMap = new HashMap<Integer, String>();

    static {
        codeMap.put(355, "阿尔巴尼亚");
        codeMap.put(376, "安道尔");
        codeMap.put(244, "安哥拉");
        codeMap.put(1264, "安圭拉");
        codeMap.put(1268, "安提瓜");
        codeMap.put(54, "阿根廷");
        codeMap.put(297, "阿鲁巴");
        codeMap.put(61, "澳大利亚");
        codeMap.put(43, "奥地利");
        codeMap.put(1242, "巴哈马");
        codeMap.put(973, "巴林岛");
        codeMap.put(1246, "巴巴多斯");
        codeMap.put(375, "白俄罗斯");
        codeMap.put(32, "比利时");
        codeMap.put(501, "伯利兹");
        codeMap.put(591, "玻利维亚");
        codeMap.put(267, "博茨瓦纳");
        codeMap.put(55, "巴西");
        codeMap.put(673, "文莱");
        codeMap.put(359, "保加利亚");
        codeMap.put(237, "喀麦隆");
        codeMap.put(1, "北美地区");
        codeMap.put(1345, "开曼群岛");
        codeMap.put(56, "智利");
        codeMap.put(57, "哥伦比亚");
        codeMap.put(506, "哥斯达黎加");
        codeMap.put(385, "克罗地亚");
        codeMap.put(531, "库拉索");
        codeMap.put(357, "塞浦路斯");
        codeMap.put(420, "捷克");
        codeMap.put(45, "丹麦");
        codeMap.put(1809, "多米尼加");
        codeMap.put(593, "厄瓜多尔");
        codeMap.put(20, "埃及");
        codeMap.put(503, "萨尔瓦多");
        codeMap.put(372, "爱沙尼亚");
        codeMap.put(679, "斐济");
        codeMap.put(358, "芬兰");
        codeMap.put(599, "荷属安的列斯");
        codeMap.put(49, "德国");
        codeMap.put(233, "加纳");
        codeMap.put(350, "直布罗陀");
        codeMap.put(30, "希腊");
        codeMap.put(1473, "格林纳达");
        codeMap.put(590, "瓜德罗普岛");
        codeMap.put(1671, "关岛");
        codeMap.put(502, "危地马拉");
        codeMap.put(509, "海地");
        codeMap.put(31, "荷兰");
        codeMap.put(504, "洪都拉斯");
        codeMap.put(852, "香港");
        codeMap.put(36, "匈牙利");
        codeMap.put(354, "冰岛");
        codeMap.put(91, "印度");
        codeMap.put(62, "印尼");
        codeMap.put(353, "爱尔兰");
        codeMap.put(972, "以色列");
        codeMap.put(39, "意大利");
        codeMap.put(1876, "牙买加");
        codeMap.put(81, "日本");
        codeMap.put(962, "约旦");
        codeMap.put(254, "肯尼亚");
        codeMap.put(965, "科威特");
        codeMap.put(856, "老挝");
        codeMap.put(371, "拉脱维亚");
        codeMap.put(961, "黎巴嫩");
        codeMap.put(266, "莱索托");
        codeMap.put(370, "立陶宛");
        codeMap.put(352, "卢森堡");
        codeMap.put(389, "马其顿");
        codeMap.put(60, "马来西亚");
        codeMap.put(356, "马耳他");
        codeMap.put(596, "马提尼克岛");
        codeMap.put(230, "毛里求斯");
        codeMap.put(52, "墨西哥");
        codeMap.put(373, "摩尔多瓦");
        codeMap.put(382, "黑山");
        codeMap.put(212, "摩洛哥");
        codeMap.put(258, "莫桑比克");
        codeMap.put(264, "纳米比亚");
        codeMap.put(687, "新喀里多尼亚");
        codeMap.put(64, "新西兰");
        codeMap.put(505, "尼加拉瓜");
        // codeMap.put(1, "北马里亚纳群岛");
        codeMap.put(47, "挪威");
        codeMap.put(968, "阿曼");
        codeMap.put(507, "巴拿马");
        codeMap.put(595, "巴拉圭");
        codeMap.put(51, "秘鲁");
        codeMap.put(63, "菲律宾");
        codeMap.put(48, "波兰");
        codeMap.put(351, "葡萄牙");
        codeMap.put(1809, "波多黎各");
        codeMap.put(974, "卡塔尔");
        codeMap.put(40, "罗马尼亚");
        codeMap.put(966, "沙特阿拉伯");
        codeMap.put(381, "塞尔维亚");
        codeMap.put(65, "新加坡");
        codeMap.put(421, "斯洛伐克");
        codeMap.put(386, "斯洛文尼亚");
        codeMap.put(27, "南非");
        codeMap.put(82, "韩国");
        codeMap.put(33, "法国");
        codeMap.put(94, "斯里兰卡");
        codeMap.put(1869, "圣基茨");
        codeMap.put(597, "苏里南");
        codeMap.put(268, "斯威士兰");
        codeMap.put(46, "瑞典");
        codeMap.put(41, "瑞士");
        codeMap.put(66, "泰国");
        codeMap.put(1868, "特立尼达");
        codeMap.put(216, "突尼斯");
        codeMap.put(90, "土耳其");
        codeMap.put(1649, "特克斯和凯科斯群岛");
        codeMap.put(971, "阿联酋");
        codeMap.put(44, "英国");
        codeMap.put(380, "乌克兰");
        codeMap.put(598, "乌拉圭");
        codeMap.put(58, "委内瑞拉");
        codeMap.put(967, "也门");
        codeMap.put(260, "赞比亚");
        codeMap.put(263, "津巴布韦");
        // codeMap.put(1, "美国");
        codeMap.put(265, "马拉维");
        codeMap.put(34, "西班牙");
        codeMap.put(886, "台湾");
        codeMap.put(377, "摩纳哥");
        codeMap.put(994, "阿塞拜疆");
        codeMap.put(229, "贝宁");
        codeMap.put(599, "荷兰加勒比区");
        codeMap.put(1284, "英属维尔京群岛");
        codeMap.put(226, "布基纳法索");
        codeMap.put(236, "中非");
        codeMap.put(235, "查德");
        codeMap.put(682, "库克群岛");
        codeMap.put(225, "科特迪瓦");
        codeMap.put(240, "赤道几内亚");
        codeMap.put(298, "法罗群岛");
        codeMap.put(594, "法属圭亚那");
        codeMap.put(689, "法属波利尼西亚");
        codeMap.put(241, "加蓬");
        codeMap.put(995, "格鲁吉亚");
        codeMap.put(224, "几内亚");
        codeMap.put(218, "利比亚");
        codeMap.put(853, "澳门");
        codeMap.put(261, "马达加斯加岛");
        codeMap.put(223, "马里");
        codeMap.put(227, "尼日尔");
        codeMap.put(234, "尼日利亚");
        codeMap.put(92, "巴基斯坦");
        codeMap.put(675, "巴布亚新几内亚");
        codeMap.put(262, "留尼汪");
        codeMap.put(1784, "圣文森特和格林纳丁斯");
        codeMap.put(685, "萨摩亚");
        codeMap.put(239, "圣多美和普林西比");
        codeMap.put(221, "塞内加尔");
        codeMap.put(248, "塞舌尔");
        codeMap.put(677, "所罗门群岛");
        codeMap.put(255, "坦桑尼亚");
        codeMap.put(220, "冈比亚");
        codeMap.put(228, "多哥");
        codeMap.put(676, "汤加");
        codeMap.put(1340, "美属维尔京群岛");
        codeMap.put(678, "瓦努阿图");
        codeMap.put(84, "越南");
        codeMap.put(531, "库拉索");
        codeMap.put(238, "佛得角");
        codeMap.put(213, "阿尔及利亚");
        codeMap.put(256, "乌干达");
        codeMap.put(374, "亚美尼亚");
        codeMap.put(691, "密克罗尼西亚");
        codeMap.put(680, "帕劳");
        codeMap.put(269, "马约特");
        codeMap.put(423, "列支敦士登");
        codeMap.put(590, "圣巴泰勒米岛");

        codes = codeMap.keySet().toArray(new Integer[]{});

        Arrays.sort(codes, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        });

    }

    /**
     * 根据区号获取国家
     */
    public static String getCountryCode(String phone) {

        for (Integer code : codes) {
            if (phone.startsWith(code.toString())) {
                return code.toString();
            }
        }

        return "";
    }

    public static String getCountryByPhoneCode(String phoneCode) {
        return codeMap.get(Integer.parseInt(phoneCode));
    }

    public static void main(String[] args) {
        String s = "852";
        System.out.println(CountryLocationUtil.getCountryByPhoneCode(s));
    }
}
