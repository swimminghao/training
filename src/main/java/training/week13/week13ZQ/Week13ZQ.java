package training.week13.week13ZQ;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Copyright (C), 2018-2019 善诊
 * Week13ZQ
 *
 * @Author: zhuqiang
 * @Date: 2019-11-27
 */
public class Week13ZQ {

    public static Iterable<String> parse(Iterable<String> json) {


        Iterator<String> iterator = json.iterator();
        return () -> new Iterator<String>() {
            private List<Json> jsons = new ArrayList<>();

            @Override
            public boolean hasNext() {

                return iterator.hasNext();
            }

            @Override
            public String next() {
                JSONObject jsonObject = JSON.parseObject(iterator.next());
                jsons = combine(jsons, parse(jsonObject)).stream().filter(Json.distinctByKey(Json::getFieldName)).collect(Collectors.toList());
                return JSON.toJSONString(jsons);
            }
        };
    }

    static List<Json> combine(List<Json> jsons, List<Json> jsonList) {

        jsons.stream().forEach(e -> jsonList.stream().forEach(c -> {
                    if (e.getFieldName().equals(c.getFieldName())&&(CollectionUtils.isNotEmpty(e.getJsons())||CollectionUtils.isNotEmpty(c.getJsons()))) {
                       e.addAllJsons(combine(e.getJsons(), c.getJsons())) ;
                    }
                }
        ));
        jsons.addAll(jsonList);
        return jsons;
    }


    private static List<Json> parse(JSONObject jsonObject) {
        List<Json> jsons = new ArrayList<>();
        jsonObject.entrySet().iterator().forEachRemaining(e -> {
                    JsonObjectFiledType jsonFiledType = JsonObjectFiledType.getByValue(e.getValue());
                    if (ObjectUtils.allNotNull(jsonFiledType)) {
                        Json json = jsonFiledType.buildJson(e.getKey(), jsons);
                        if (jsonFiledType.isHasNext()) {
                            if (e.getValue() instanceof JSONObject) {
                                json.addAllJsons(parse((JSONObject) e.getValue()));
                            }
                            if (e.getValue() instanceof JSONArray) {

                                json.addAllJsons(parseArray((JSONArray) e.getValue()));
                            }
                        }
                    }
                }
        );
        return jsons;
    }

    private static List<Json> parseArray(JSONArray array) {
        for (Object object : array) {
            if (object instanceof JSONObject) {
                return parse((JSONObject) object);
            }
        }
        return new ArrayList<>();
    }



    public static void main(String[] args) {
        Iterable<String> string = parse(Arrays.asList("{\"_test\":{\"null\":null,\"ARRAY-empty\":[],\"ARRAY-ARRAY\":[[0.6430615715386766]],\"ARRAY-OBJECT\":[{\"f1\":\"v1\",\"f2\":\"v2\"}],\"ARRAY-null\":[null],\"BOOLEAN\":true,\"DECIMAL\":0.9918221223524248,\"INTEGER\":256351273,\"ARRAY-0\":[],\"ARRAY-1\":[1406543306,73707832],\"ARRAY-2\":[3507808389422058496,6024250555740930048],\"ARRAY-3\":[1406543306,73707832,3507808389422058496,6024250555740930048],\"ARRAY-4\":[0.7687675764181222,0.3247625989665921,0.7354989849421176,0.2990918446921703,0.4255704127275035],\"ARRAY-5\":[1406543306,73707832,0.7687675764181222,0.3247625989665921,0.7354989849421176,0.2990918446921703,0.4255704127275035],\"ARRAY-6\":[3507808389422058496,6024250555740930048,0.7687675764181222,0.3247625989665921,0.7354989849421176,0.2990918446921703,0.4255704127275035],\"ARRAY-7\":[1406543306,73707832,3507808389422058496,6024250555740930048,0.7687675764181222,0.3247625989665921,0.7354989849421176,0.2990918446921703,0.4255704127275035]},\"metadata\":{\"url\":\"http://baike.baidu.com/item/%E8%B4%B9%E5%B0%94%E8%92%82%E7%BB%BC%E5%90%88%E5%BE%81/10059866\"},\"createTime\":1574414532085,\"updateTime\":1574415010135,\"_id\":\"10059866\",\"content\":{\"summary\":\"费尔蒂综合征（Felty综合征）是指除有典型的 类风湿关节炎临床表现外，还伴有脾脏肿大和白细胞计数减少的一种严重型类风湿关节炎。白细胞计数减少的原因与 脾功能亢进，或存在针对中性粒细胞的特异抗体，或存在骨髓抑制因子等有关。这些病人通常有高滴度的类风湿因子和抗核抗体，有皮下结节和类风湿关节炎的表现。\",\"videos\":[],\"basic\":{\"常见症状\":\"暴露部位皮肤色素沉着、  皮肤—黏膜—小腿溃疡、紫斑，心包炎、胸膜炎、肝轻度肿大、淋巴结肿大等\",\"常见发病部位\":\"肝脾，关节，皮肤，淋巴结等\",\"英文名称\":\"Felty syndrome\",\"英文别名\":\"neutropenic-hypersplenism  -arthritis\",\"别    称\":\"关节炎-粒 细胞减少-脾大综合征\",\"就诊科室\":\"风湿免疫科\",\"多发群体\":\"0～50岁以上人群\"},\"paragraphs\":[{\"texts\":[\"本病患者少数病例脾肿大和粒细胞减少可早于 类风湿关节炎症状。关节病变常较一般 类风湿关节炎严重，多有骨侵袭和畸形，但亦有轻型者。约1/3病例有非活动性滑膜炎，脾脏可从刚可触及至巨脾，大小不等。1/3病人可有中性粒细胞减少和 类风湿关节炎的典型费尔蒂综合征特点，但无脾脏肿大。\",\"本病中约60%的病人有继发感染。感染部位以皮肤和呼吸道多见。致病菌多为常见的葡萄球菌、链球菌以及革兰阴性杆菌。感染可能与粒细胞减少有关。\",\"费尔蒂综合征可伴有肝结节性再生性增殖，这种特征性的肝病变在 红斑狼疮和其他结缔组织病中很少见到。组织学上肝受累见于60%的费尔蒂病人，可有肝功异常，部分病人组织学上呈现异常，但肝功能正常。\",\"关节表现与典型的 类风湿关节炎无明显区别。常于关节症状出现数月到数年后才出现典型症状，因而病人年龄多在40～50岁以上。暴露部位皮肤色素沉着、皮肤—黏膜—小腿溃疡、紫斑，也可发生 干燥综合征、 心包炎、 胸膜炎、周围 神经病变、肝轻度肿大、淋巴结肿大、体重减轻等。\"],\"title\":\"临床表现\"},{\"texts\":[\"1.实验室检查\",\"血液系统各细胞系均有变化。除 类风湿关节炎常见的由血清铁结合力降低所致的轻度 贫血症外，红细胞寿命缩短亦为其特征。血小板轻度下降。粒细胞减少极为突出，严重者可低至0.1×10 9/L以下。\",\"2.其他辅助检查\",\"（1）骨髓象 显示有粒细胞成熟障碍。\",\"（2）免疫学检查 类风湿因子及抗核抗体常为阳性。\"],\"title\":\"检查\"},{\"texts\":[\"具有类风湿关节炎、 脾大及粒细胞减少三个主征，再结合其他免疫检查即可确定诊断，但还应结合临床表现，病史和体格检查进一步确立诊断。\"],\"title\":\"诊断\"},{\"texts\":[\"1.激素\",\"通常列为首选药物，但疗效仅为一过性，很少完全缓解。近期报道用激素冲击疗法可获显效。\",\"2.脾切除\",\"对激素治疗无效，而粒细胞数又低于1.0×10 9/L，伴有严重 贫血（溶血性）或血小板减少、反复感染者宜行 脾切除术。80%病人术后可获得血液学改善，且反复感染与小腿溃疡亦多有好转。\",\"3.其他疗法\",\"包括抗风湿制剂、青霉胺、金制剂均可试用。但由于粒细胞过低，使用免疫抑制剂要慎重。近期报道，激素同雷公藤联合使用，可使病情缓解。\",\"4.对症治疗\",\"用抗原性尽可能小的抗生素控制感染，因为许多抗生素可加重体内已存在的免疫反应，故应用抗生素治疗本病感染须慎重选用。\"],\"title\":\"治疗\"},{\"texts\":[\"本病常较一般类风湿关节炎严重，多有骨侵袭和畸形。约60%的病人有继发感染，感染部位以皮肤和呼吸道多见，肝功能异常。大部分病人有轻至中度的 贫血，预后不佳。\"],\"title\":\"预后\"}],\"title\":\"费尔蒂综合征\",\"articles\":[{\"title\":\"类风湿关节炎有哪些临床分型？\",\"url\":\"http://www.baikemy.com/jiankangkepu/5057126999553\"},{\"title\":\"类风湿关节炎的起病方式可分为３种：①隐匿型：占类风湿关节炎的６０％～７０％，典型起病常于数周或数月内逐渐起病，表现为掌指关节和腕关节的疼痛、肿胀和僵硬，可伴全身不适和乏力、低热、食欲不振、体重下降等；②急性型：占类风湿关节炎的８％～１５％，病人能明确指出症状出现的具体日期或在某几天之内，表现为...\",\"url\":\"http://www.baikemy.com/jiankangkepu/5057126999553\"},{\"title\":\"类风湿关节炎有哪些关节外表现？\",\"url\":\"http://www.baikemy.com/jiankangkepu/2005224669953\"},{\"title\":\"类风湿关节炎的关节外表现包括：（1）皮肤：近20%的类风湿关节炎出现皮下结节，称为类风湿结节，常对称地出现于肘关节皮下鹰嘴突附近、膝关节上下及四肢肌腱部。（2）肺：表现为胸膜炎、弥漫性间质性肺炎或肺动脉高压，有时为无临床症状的双侧肺内类风湿结节。（3）心脏：类风湿关节炎可以出现心包炎，心包积液...\",\"url\":\"http://www.baikemy.com/jiankangkepu/2005224669953\"}],\"tags\":[\"科学百科疾病症状分类\",\"科学\",\"疾病\",\"学科\"]},\"md5\":\"18773b214b569a23ab8d622d5cbbfd56\"}\n"
                , "{\"_test\":{\"null\":null,\"ARRAY-empty\":[],\"ARRAY-ARRAY\":[[0.3364945281500248]],\"ARRAY-OBJECT\":[{\"f1\":\"v1\",\"f2\":\"v2\"}],\"ARRAY-null\":[null],\"BOOLEAN\":false,\"DECIMAL\":0.21109097983385705,\"INTEGER\":660757348,\"ARRAY-0\":[],\"ARRAY-1\":[1315759675,264811889],\"ARRAY-2\":[3730709536061364224,8538708169176432640,6750166952847928320,12602145026362368],\"ARRAY-3\":[1315759675,264811889,3730709536061364224,8538708169176432640,6750166952847928320,12602145026362368],\"ARRAY-4\":[0.919518390803765,0.4672041607231492,0.8482402178673453,0.3865613838921471],\"ARRAY-5\":[1315759675,264811889,0.919518390803765,0.4672041607231492,0.8482402178673453,0.3865613838921471],\"ARRAY-6\":[3730709536061364224,8538708169176432640,6750166952847928320,12602145026362368,0.919518390803765,0.4672041607231492,0.8482402178673453,0.3865613838921471],\"ARRAY-7\":[1315759675,264811889,3730709536061364224,8538708169176432640,6750166952847928320,12602145026362368,0.919518390803765,0.4672041607231492,0.8482402178673453,0.3865613838921471]},\"metadata\":{\"url\":\"http://baike.baidu.com/item/%E8%86%9C%E5%A2%9E%E7%94%9F%E6%80%A7%E8%82%BE%E5%B0%8F%E7%90%83%E8%82%BE%E7%82%8E/10001571\"},\"createTime\":1574414531890,\"updateTime\":1574415009902,\"_id\":\"10001571\",\"content\":{\"summary\":\"膜增生性肾小球 肾炎(MPGN)，是肾小球肾炎中最少见的类型之一，一般分为原发性和继发性。此病曾用名包括系膜毛细血管性肾小球肾炎(MCGN)、系膜毛细血管增生性肾炎、小叶性肾炎、低补体血症性肾炎等。本病是一种具有特定病理形态及免疫学表现的综合征。临床主要表现为肾炎、 肾病或肾炎肾病同时存在和低补体血症；组织学上可见系膜增生，毛细血管壁增厚，肾小球呈分叶状，故又称分叶性肾炎。其病理改变的主要特点是系膜细胞增生、毛细血管壁增厚及基底膜双轨。根据电镜下电子致密物沉积的部位可将MPGN分为3型：Ⅰ型为内皮下致密物沉积；Ⅱ型为特征性基膜内致密物沉积；Ⅲ型是上皮下和内皮下致密物同时出现；大多认为Ⅱ型是一种独特的 肾小球疾病，Ⅲ型可能是Ⅰ型膜增生性肾炎的一种变异。本病临床主要特征是大多患者有持续性低补体血症，因而病程较长，难以治愈且预后差。\",\"videos\":[],\"basic\":{\"常见症状\":\"蛋白尿，血尿，正细胞性贫血等\",\"常见发病部位\":\"肾\",\"英文名称\":\"membranoproliferative  glomerulonephritis\",\"就诊科室\":\"肾内科\",\"常见病因\":\"与遗传有关\"},\"paragraphs\":[{\"texts\":[\"原发性膜增生性 肾炎病因不明，一般认为Ⅰ型为免疫复合物病；Ⅱ型为免疫复合物及自身抗体性疾病，可能与遗传有关。\",\"继发性膜增生性 肾炎中混合性 冷球蛋白血症有3种亚型。Ⅰ型 冷球蛋白血症是单株峰球蛋白，通常为骨髓瘤蛋白。Ⅱ型通常为单株峰IgM球蛋白结合IgG，又称抗IgG类因子，而Ⅲ型则是多株峰免疫球蛋白。Ⅱ型和Ⅲ型 冷球蛋白血症易发生肾损害。MPGN的病因与发病机制不十分明确，Ⅰ型MPGN认为是免疫复合物病，由相对大的难溶的免疫复合物反复持续沉积引起，Ⅱ型MPGN患者血清中也存在免疫复合物，冷球蛋白补体异常、血清C3持续降低。均提示免疫复合物在Ⅱ型MPGN中的作用。Ⅱ型MPGN患者血清中可检出C3 肾炎因子(C3NeF)，C3NeF是C3bBb转化酶的自身抗体，使C3bBb作用加强导致补体旁路持续激活，产生持续低补体血症和基膜变性，所以补体代谢障碍为中心环节。\",\"另外，Ⅱ型MPGN 肾移植中常复发，可能因病人血清中存在能引起异常糖蛋白形成的物质沉积于基底膜而导致 肾炎。\",\"本病可能与遗传有关，Ⅱ型MPGN患者常出现HLA-B7，大多Ⅰ型MPGN病人具有特殊B细胞同种抗原。\"],\"title\":\"病因\"},{\"texts\":[\"本组疾病在原发性肾小球病中较少见，也是 肾病综合征中为数不多的增殖性 肾炎之一。各种病理类型的临床表现基本相似，无论本病的临床表现为何种综合征，几乎都有蛋白尿和 血尿同时存在，蛋白尿为非选择性， 血尿常为镜下持续性 血尿，有10%～20%患者常于呼吸道感染后出现发作性肉眼 血尿，为严重的、多样尿红细胞畸形的肾小球源性 血尿。约1/3以上患者伴有 高血压， 高血压的程度一般比较轻，但也有个别病例，尤其是Ⅱ型患者，可能发生严重的 高血压，大剂量的激素治疗也有可能诱发 高血压危象。至少有半数的患者出现急性或慢性肾功能不全，在发病初期出现肾功能不全常提示预后不良。患者常于起病后即有较严重的正细胞正色素性 贫血，表现为面色苍白，气短、乏力，并且 贫血的程度与肾功能减退程度不成比例。\",\"本病发病时，至少有1/2的患者表现为 肾病综合征；约1/4的患者表现为无症状性血尿和蛋白尿；还有1/4～1/3的患者表现为急性肾炎综合征，伴有红细胞及红细胞管型尿、 高血压和肾功能不全。约有一半的患者可有前驱呼吸道感染史，40%在起病前有抗“O”滴度升高和 链球菌感染的其他证据。有的患者可发生部分脂质 营养不良，尤其是Ⅱ型病变，甚至可以在还没有肾脏病临床表现时发生。某些患者可显示X-连锁遗传。先天性的补体和a1-抗胰蛋白酶缺乏也易发生在本病Ⅰ型。在 肾病综合征时可发生肾 静脉血栓形成。尽管本病发展有高度的个体差异性，但本病病情总体上呈缓慢的进行性进展。临床上Ⅱ型更倾向于表现为肾炎征象，新月体肾炎和急性肾衰的伴发率高，而Ⅰ型具有更多 肾病的特征，常有先驱感染和 贫血；Ⅱ型患者血清常常有持续的低补体血症，并且发病年龄较小，几乎所有患者发病均在20岁以下，此外Ⅱ型更容易在 肾移植后复发。\",\"Ⅲ型很少见，主要发生在儿童和青年，10～20岁为高峰，男女发病接近。该型的临床表现基本与Ⅰ型的长期临床改变相似。据Strife的描述Ⅲ型有C3水平降低，但无C3肾炎因子。非肾综性蛋白尿的预后比 肾病综合征表现者要好，该型进入终末期肾病的个体差异比较大，在长期的病程中有些患者病情可以比较稳定甚至逐渐改善。\"],\"title\":\"临床表现\"},{\"texts\":[\"本病诊断的主要依据是病理检查结果，电镜和免疫荧光检查可以区分Ⅰ型和Ⅱ型。持续性的低补体血症，持续无选择性的蛋白尿(或肾病综合征)伴有严重的多样畸形的红细胞尿与肾功能下降不成比例的 贫血，常提示该病发生。C3肾炎因子和血补体C3同时降低常提示病情活动。\"],\"title\":\"诊断\"},{\"texts\":[\"诊断MPGN需要排除所有继发性因素，如乙型肝炎或丙型肝炎、 艾滋病、其他感染或结缔组织病。MPGN的诊断主要通过组织病理学的检查，随着丙型肝炎相关性MPGN、HIV相关MPGN的日趋流行，对表面上看似原发性MPGN的患者必须做相应的血清学检查。常见需要鉴别的疾病有：\",\"1.糖尿病肾病\",\"MPGN的结节状损害出现在大多数肾小球中，而 糖尿病肾病发生结节状损害的小球相对较少，另外从免疫病理学上可以进行鉴别。\",\"2.淀粉样变肾病\",\"HE刚果红染色及电镜下完全可以鉴别。\",\"3.轻链肾炎\",\"光镜下与MPGN难以鉴别，免疫病理学可以明确区分。\",\"4.狼疮性肾炎\",\"慢性低补体血症应与狼疮性肾炎进行鉴别，狼疮性肾炎可以出现多种类型的病理学改变，如可出现类似于Ⅰ、Ⅲ型MPGN样的改变，但狼疮性肾炎在肾小球内可有IgG、IgM、IgA、C3、C4、C1q的沉积，即“满堂亮”表现，而MPGN同时出现多种免疫球蛋白及补体沉积的情形罕见。\",\"5.过敏性紫癜肾炎\",\"可出现类似于MPGN样的病理变化，鉴别的要点为 过敏性紫癜肾炎肾小球系膜区及毛细血管襻上有大量的IgA沉积，还可表现出皮肤紫癜、关节痛和腹痛等。\",\"6.感染后肾炎\",\"感染后肾炎与MPGN的Ⅰ型有时难以鉴别，但一般感染后肾炎的病程比较短。偶尔感染后肾炎也可发展为MPGN。\"],\"title\":\"鉴别诊断\"},{\"texts\":[\"本病所致肾病综合征的治疗常常比较困难。小剂量、隔天泼尼松治疗可能有利于改善肾功能。West等使用隔天口服激素长期治疗，在治疗前后比较肾活检，结果证明此法有利于肾脏的存活。大部分肾病学家仅做对症治疗。\",\"Ⅰ型的治疗，除糖皮质激素外，还可用其他药物如免疫抑制药和抗凝剂。\",\"对于各年龄段MPGN患者，如肾功能正常且仅表现为无症状轻度蛋白尿时，无须接受激素免疫抑制药治疗，仅需每3～4个月随访1次，密切观察肾功能、蛋白尿和血压控制情况。\",\"成人和儿童原发性MPGN患者，在尿蛋白>3克/天，肾功能损害及活检发现肾间质病变时，方可给予激素、免疫抑制药治疗。如果无效则停止服用糖皮质激素。建议密切随访，着重保守治疗(即控制血压、应用降低尿蛋白药物和纠正代谢紊乱)。重视能够延缓肾功能衰退的因素和密切随访。\",\"其他治疗包括降脂，应用ACEI、ARB、低分子肝素等，近年有学者报道用霉酚酸脂(MMF)治疗本病，显示初步效果，但病例数尚少，且缺乏对照和长期观察研究。\",\"另外细胞毒药物应用、血浆置换方法、中药治疗在一些研究治疗中获得一些疗效。\",\"临床医生在决定什么类型的患者何时进行治疗时必须考虑疾病的预期病程和结局，以及治疗的利弊，肾功能不全的进行性发展和药物治疗引起的依从性差等因素。\"],\"title\":\"治疗\"},{\"texts\":[\"大量研究证实原发性MPGN 10年肾存活率达60%～65%，而且各型MPGN病程及预后类似。肾病综合征(大量蛋白尿)和出现肾间质病变是预后不良的主要征兆。临床表现为持续性高血压、GFR下降的肾病综合征预后差。发病年龄小预后良好；成年人发病者病变进行性加重，预后不良。肾小球系膜细胞增生、基底膜增厚与预后无明显关系，而局灶性新月体形成的多少及间质改变的轻重程度与预后明显相关，新月体形成及严重的肾小管间质病变预后差。约50%表现为肾病综合征的病人10年内发展为ESRF，50%病人肾病综合征持续数年后消失，肾功能正常。 肾移植后，本病可再发，但小于10%病人导致移植肾丧失。接受糖皮质激素药物、免疫抑制药及抗凝药物等联合治疗者，肾功能可保持稳定或得到较好改善。\",\"总之，本病进入终末期肾病的个体差异比较大，Ⅰ型患者通常1/3可以自发缓解，1/3呈进行性发展，还有1/3疾病迁延进展缓慢，但一直不能完全缓解。\",\"文献资料中提示原发性Ⅰ型病变的预后不良因素有：高血压，肾功能损害，肾病综合征范围的蛋白尿形成，肾活检时发现细胞性新月体，合并动脉病变，有肾小管及间质的损害。\",\"Ⅱ型较Ⅰ型的预后差这可能是由于Ⅱ型为致密物沉积疾病，肾活检常会发现新月体和小管间质病变。Ⅱ型很少发生临床缓解，儿童患者的临床缓解率不足5%。患者通常在病程的第8～12年进入肾功能衰竭，Ⅱ型患者在做 肾移植以后常常会复发，尤其是 肾移植前活检就发现有新月体改变的患者。Ⅰ型在肾移植后也可能出现再发，但是没有Ⅱ型频繁。\"],\"title\":\"预后\"},{\"texts\":[\"本病3型病程转归基本相同，预防要从自身健康着手，平时避免劳累，合理饮食，科学锻炼，增强体质，提高机体免疫力，以防疾病发生。对于已患和出现并发症的病人，应对原发病及并发症进行积极有效的预防和治疗。一旦发现感染，应及时选用对致病菌敏感、强效且无肾毒性的抗生素治疗，有明确感染灶者应尽快去除，以防肾功能不全的进行性发展。\"],\"title\":\"预防\"}],\"title\":\"膜增生性肾小球肾炎\",\"articles\":[{\"title\":\"感冒会引起肾炎吗?\",\"url\":\"http://www.baikemy.com/jiankangkepu/2421999101953\"},{\"title\":\"急性肾炎的特点是起病急、出现血尿、蛋白尿、少尿，以及常伴有高血压、水肿等症状或体征。由于人们的呼吸道内，尤其在鼻腔，咽等部位常常“隐居”着各种病毒和病菌，其中包括β-溶血性链球菌、葡萄球菌等，它们在人体健康而有足够抵抗力时是不会引 起疾病的，但人们在感冒时，往往抵抗力下降，它们便伺机活跃起来，...\",\"url\":\"http://www.baikemy.com/jiankangkepu/2421999101953\"},{\"title\":\"延缓肾脏病进展三步曲\",\"url\":\"http://www.baikemy.com/jiankangkepu/4987788672001\"},{\"title\":\"因为多数慢性肾脏病（CKD）起病隐匿，大多数肾脏病早期几乎都没有任何症状；即使有症状，也可能不是肾脏病所特有的症状，例如肾功能不全早期，由于毒素 的影响，患者可能最先出现的症状是食欲不振，恶心，呕吐，四肢乏力，面色发黄等；还有慢性肾脏病的高危因素，如慢性扁桃体炎，肥胖，高血压病，糖尿病等。 慢...\",\"url\":\"http://www.baikemy.com/jiankangkepu/4987788672001\"},{\"title\":\"肾脏代偿能力强，早期病情被忽视\",\"url\":\"http://www.baikemy.com/jiankangkepu/6356618361089\"},{\"title\":\"临床上见过很多肾脏病患者，初次到医院检查，就发现病情已经很严重了，部分患者诊断为尿毒症，需要透析治疗。鉴于这种情况：①是由于肾脏的强大代偿能力，只要肾脏受到的损伤不是很严重，那部分没有受损的肾脏组织就会自动代偿性工作，担负起受损肾脏组织的功能，正是基于肾脏强大的代偿能力，慢性肾病往往不容易在早...\",\"url\":\"http://www.baikemy.com/jiankangkepu/6356618361089\"},{\"title\":\"肾脏病的早期表现和预防\",\"url\":\"http://www.baikemy.com/jiankangkepu/6356511072769\"},{\"title\":\"肾脏病是我国疾病死亡十大病因之一，肾脏病有许多不同的种类，究竟其早期较常出现的表现有哪些？水肿最敏感的指标是体重骤然增加，此时不一定有水肿的表现，称为隐性水肿。假如水肿进一步加重，开始可见晨起眼皮浮肿，午后多消退，劳累后加重，休息后减轻，继而可出现身体低垂部位水肿。年轻患者如没有高血压病的家族...\",\"url\":\"http://www.baikemy.com/jiankangkepu/6356511072769\"}],\"tags\":[\"科学百科疾病症状分类\",\"科学\",\"疾病\",\"学科\",\"医学术语\"]},\"md5\":\"3d9a38986b6f74f56dd5fade8fb2fb10\"}"
        ));

        Iterator iterator = string.iterator();

        for (String s :
                string) {
            System.out.println(s);
        }

    }
}

@Data
@Accessors(chain = true)
class Json {
    String fieldName;
    List<JsonFiledType> fieldType;
    List<Json> jsons = new ArrayList<>();

    public void addAllJsons(List<Json> jsons) {
        this.jsons.addAll(jsons);
        this.jsons=this.jsons.stream().filter(distinctByKey(Json::getFieldName)).collect(Collectors.toList());
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return object -> seen.putIfAbsent(keyExtractor.apply(object), Boolean.TRUE) == null;
    }

}

enum JsonFiledType {
    Object,
    Array,
    String,
    Float,
    Integer,
    Long,
    Boolean,
}

@AllArgsConstructor
enum JsonObjectFiledType implements JsonProcess {
    JSONObject() {
        @Override
        public Json buildJson(java.lang.String key, List<Json> value) {
            return commonBuildJson(key, value, JsonFiledType.Object);
        }

        @Override
        public Boolean instance(Object o) {
            return o instanceof JSONObject;
        }
    },
    JSONArray() {
        @Override
        public Json buildJson(java.lang.String key, List<Json> value) {
            return commonBuildJson(key, value, JsonFiledType.Array);
        }

        @Override
        public Boolean instance(Object o) {

            return o instanceof JSONArray;
        }
    },
    BigDecimal() {
        @Override
        public Json buildJson(java.lang.String key, List<Json> value) {
            return commonBuildJson(key, value, JsonFiledType.Float);
        }

        @Override
        public Boolean instance(Object o) {

            return o instanceof BigDecimal;
        }
    },
    String() {
        @Override
        public Json buildJson(java.lang.String key, List<Json> value) {
            return commonBuildJson(key, value, JsonFiledType.String);
        }

        @Override
        public Boolean instance(Object o) {

            return o instanceof String;
        }
    },
    Integer() {
        @Override
        public Json buildJson(java.lang.String key, List<Json> value) {
            return commonBuildJson(key, value, JsonFiledType.Integer);
        }

        @Override
        public Boolean instance(Object o) {

            return o instanceof Integer;
        }
    },
    Long() {
        @Override
        public Json buildJson(java.lang.String key, List<Json> value) {
            return commonBuildJson(key, value, JsonFiledType.Long);
        }

        @Override
        public Boolean instance(Object o) {
            return o instanceof Long;
        }
    },
    ;


    Json commonBuildJson(String key, List<Json> jsons, JsonFiledType jsonFiledType) {
        Json json = new Json().setFieldName(key).setFieldType(Arrays.asList(jsonFiledType));
        jsons.add(json);
        return json;

    }

    public static JsonObjectFiledType getByValue(Object o) {
        for (JsonObjectFiledType filedType : values()) {
            if (filedType.instance(o)) {
                return filedType;
            }
        }
        return null;
    }

    public Boolean isHasNext() {
        return null != this && (JsonObjectFiledType.JSONObject.name().equals(this.name()) || JsonObjectFiledType.JSONArray.name().equals(this.name()));
    }
}

interface JsonProcess {
    Json buildJson(String key, List<Json> value);

    Boolean instance(Object o);
}
