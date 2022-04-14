package com.example;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.example.mapper.BikeMapper;
import com.example.mapper.CityMapper;
import com.example.pojo.*;
import com.example.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.geotools.geojson.GeoJSON;
import org.geotools.geojson.GeoJSONUtil;
import org.geotools.geojson.geom.GeometryJSON;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.Feature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.io.*;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
class ShareBikeWebSpringApplicationTests {

//    @Autowired
//    private CityMapper cityMapper;
//
//    @Test
//    void contextLoads() {
//        List<City> cities = cityMapper.selectAll();
//        System.out.println(cities.size());
//        cities.forEach(System.out::println);
//    }
//
//    @Autowired
//    private DruidDataSource dataSource;
//    @Test
//    public void connection() throws SQLException, IOException {
//        //select
//        //        String geojson="{\"type\": \"MultiPolygon\", \"coordinates\": [[[[119.533, 31.1591], [119.546, 31.1478], [119.56, 31.1407], [119.571, 31.129], [119.574, 31.114], [119.582, 31.1087], [119.6, 31.1092], [119.614, 31.1292], [119.631, 31.1313], [119.638, 31.1355], [119.642, 31.1484], [119.662, 31.1599], [119.664, 31.166], [119.678, 31.1682], [119.683, 31.1605], [119.704, 31.1519], [119.713, 31.1676], [119.741, 31.1734], [119.755, 31.1708], [119.779, 31.1788], [119.793, 31.1681], [119.791, 31.1566], [119.801, 31.1563], [119.81, 31.1485], [119.823, 31.1541], [119.83, 31.1614], [119.823, 31.1658], [119.826, 31.1732], [119.838, 31.1737], [119.842, 31.1688], [119.867, 31.1683], [119.878, 31.1608], [119.9, 31.1692], [119.92, 31.1709], [120.11, 31.264], [120.174, 31.3088], [120.21, 31.3457], [120.253, 31.3661], [120.336, 31.4073], [120.356, 31.4165], [120.419, 31.4484], [120.438, 31.4488], [120.46, 31.4455], [120.481, 31.4491], [120.496, 31.4479], [120.501, 31.4575], [120.517, 31.4578], [120.516, 31.4644], [120.524, 31.4684], [120.536, 31.4671], [120.555, 31.4803], [120.548, 31.4974], [120.555, 31.5076], [120.569, 31.5124], [120.593, 31.5275], [120.596, 31.5172], [120.606, 31.5252], [120.599, 31.5485], [120.595, 31.576], [120.584, 31.5852], [120.567, 31.584], [120.564, 31.5796], [120.548, 31.5766], [120.543, 31.6017], [120.553, 31.6056], [120.557, 31.6003], [120.566, 31.6019], [120.577, 31.6142], [120.591, 31.6115], [120.601, 31.6171], [120.592, 31.6251], [120.596, 31.6314], [120.59, 31.6365], [120.596, 31.6438], [120.592, 31.6503], [120.572, 31.6558], [120.561, 31.6557], [120.569, 31.6686], [120.563, 31.6805], [120.57, 31.6889], [120.585, 31.6928], [120.601, 31.7088], [120.585, 31.7144], [120.582, 31.7276], [120.6, 31.7446], [120.595, 31.7604], [120.589, 31.7625], [120.584, 31.7821], [120.571, 31.7938], [120.558, 31.7857], [120.555, 31.794], [120.544, 31.7871], [120.532, 31.7878], [120.523, 31.8063], [120.53, 31.8147], [120.531, 31.8278], [120.514, 31.8415], [120.503, 31.8417], [120.502, 31.8521], [120.491, 31.8713], [120.469, 31.8796], [120.466, 31.89], [120.45, 31.892], [120.425, 31.8984], [120.402, 31.9074], [120.392, 31.9053], [120.379, 31.9141], [120.39, 31.9258], [120.39, 31.9322], [120.375, 31.9417], [120.369, 31.9611], [120.371, 31.9908], [120.353, 31.9806], [120.263, 31.9418], [120.237, 31.9329], [120.206, 31.9315], [120.175, 31.9338], [120.135, 31.9394], [120.065, 31.9555], [120.022, 31.9678], [120.008, 31.948], [120.007, 31.9359], [120.022, 31.9197], [120.005, 31.9119], [119.998, 31.8943], [120.015, 31.8818], [120.013, 31.8717], [120.003, 31.8592], [119.99, 31.8549], [120.0, 31.8456], [120.003, 31.8282], [120.019, 31.8228], [120.025, 31.8317], [120.045, 31.8217], [120.056, 31.8333], [120.08, 31.8475], [120.085, 31.8531], [120.099, 31.8556], [120.117, 31.855], [120.123, 31.8593], [120.144, 31.8589], [120.158, 31.8678], [120.176, 31.8702], [120.185, 31.8604], [120.173, 31.8388], [120.164, 31.8327], [120.17, 31.8173], [120.179, 31.8129], [120.174, 31.801], [120.186, 31.7865], [120.193, 31.7674], [120.202, 31.764], [120.206, 31.7534], [120.184, 31.7499], [120.179, 31.7584], [120.169, 31.7609], [120.156, 31.7568], [120.155, 31.7414], [120.161, 31.7181], [120.156, 31.7132], [120.157, 31.7038], [120.145, 31.6971], [120.143, 31.6883], [120.151, 31.6824], [120.143, 31.6761], [120.129, 31.6846], [120.124, 31.6482], [120.119, 31.6309], [120.104, 31.6288], [120.094, 31.6181], [120.076, 31.6071], [120.075, 31.5954], [120.057, 31.5804], [120.056, 31.5634], [120.069, 31.5525], [120.097, 31.5487], [120.094, 31.5419], [120.102, 31.542], [120.103, 31.5186], [120.107, 31.512], [120.113, 31.5181], [120.13, 31.5047], [120.109, 31.481], [120.106, 31.4705], [120.11, 31.4615], [120.09, 31.4547], [120.061, 31.4404], [120.055, 31.4343], [120.045, 31.4062], [120.04, 31.3781], [120.04, 31.3645], [120.045, 31.3588], [120.057, 31.3561], [120.096, 31.3525], [120.1, 31.3353], [120.09, 31.3325], [120.061, 31.3391], [120.042, 31.3459], [120.024, 31.365], [120.021, 31.383], [120.028, 31.409], [120.038, 31.4259], [120.046, 31.4798], [120.045, 31.4903], [120.036, 31.4979], [120.016, 31.5054], [120.006, 31.5033], [119.997, 31.5081], [119.996, 31.4975], [119.974, 31.5159], [119.972, 31.536], [119.948, 31.5434], [119.936, 31.5527], [119.898, 31.5467], [119.862, 31.5463], [119.848, 31.5298], [119.832, 31.5292], [119.807, 31.5485], [119.792, 31.5534], [119.769, 31.5538], [119.733, 31.5632], [119.721, 31.5569], [119.713, 31.5583], [119.71, 31.576], [119.7, 31.5766], [119.685, 31.604], [119.673, 31.6093], [119.658, 31.6093], [119.639, 31.6003], [119.643, 31.5824], [119.647, 31.5777], [119.641, 31.5693], [119.628, 31.5599], [119.613, 31.5579], [119.601, 31.5387], [119.594, 31.5322], [119.584, 31.5045], [119.567, 31.5047], [119.568, 31.4904], [119.575, 31.4808], [119.573, 31.4717], [119.565, 31.4714], [119.565, 31.4643], [119.588, 31.4667], [119.592, 31.463], [119.588, 31.4458], [119.577, 31.4307], [119.555, 31.4339], [119.553, 31.4117], [119.536, 31.4082], [119.541, 31.3901], [119.53, 31.3708], [119.528, 31.3603], [119.531, 31.3309], [119.52, 31.3182], [119.524, 31.3011], [119.535, 31.2912], [119.531, 31.2777], [119.532, 31.259], [119.523, 31.2422], [119.534, 31.2368], [119.554, 31.221], [119.553, 31.2124], [119.554, 31.1791], [119.543, 31.1755], [119.533, 31.1591]]]]}";
////        GeometryJSON geometryJSON =new GeometryJSON();
////        Geometry geometry = geometryJSON.read(new StringReader(geojson));
////        System.out.println(geometry);
////        DruidPooledConnection connection = dataSource.getConnection();
////        PreparedStatement statement = connection.prepareStatement("SELECT cityid,cityname,ST_ASGEOJSON(geom) as geom,parentid,ST_ASGEOJSON(centerpoint) as centerpoint FROM `tb_city`");
////        ResultSet resultSet = statement.executeQuery();
////        while (resultSet.next()) {
////            String geom = resultSet.getObject("geom").toString();
////            Geometry geometry = geometryJSON.read(geom);
////            System.out.println(geometry.getClass().getName());
////        }
////        statement.close();
////        dataSource.close();
//
//        //insert
//        String json = "{\n" +
//                "      \"type\": \"MultiPolygon\",\n" +
//                "      \"coordinates\": [\n" +
//                "          [\n" +
//                "              [\n" +
//                "                  [119.174931, 34.093494],\n" +
//                "                  [119.166849, 34.10746],\n" +
//                "                  [119.176166, 34.118579],\n" +
//                "                  [119.168421, 34.129654],\n" +
//                "                  [119.15641, 34.133618],\n" +
//                "                  [119.150348, 34.140555],\n" +
//                "                  [119.142828, 34.134049],\n" +
//                "                  [119.129077, 34.145768],\n" +
//                "                  [119.145409, 34.158692],\n" +
//                "                  [119.141874, 34.161578],\n" +
//                "                  [119.144287, 34.176265],\n" +
//                "                  [119.136991, 34.177858],\n" +
//                "                  [119.13295, 34.205415],\n" +
//                "                  [119.099667, 34.208042],\n" +
//                "                  [119.09703, 34.232921],\n" +
//                "                  [119.09108, 34.23688],\n" +
//                "                  [119.091866, 34.262353],\n" +
//                "                  [119.074074, 34.265107],\n" +
//                "                  [119.082774, 34.310868],\n" +
//                "                  [119.073008, 34.331763],\n" +
//                "                  [119.075477, 34.342681],\n" +
//                "                  [119.062232, 34.338683],\n" +
//                "                  [119.057068, 34.356176],\n" +
//                "                  [119.056227, 34.3823],\n" +
//                "                  [119.065263, 34.409232],\n" +
//                "                  [119.086871, 34.409747],\n" +
//                "                  [119.089565, 34.42087],\n" +
//                "                  [119.061222, 34.420183],\n" +
//                "                  [119.041409, 34.421901],\n" +
//                "                  [119.038659, 34.403735],\n" +
//                "                  [119.033215, 34.398881],\n" +
//                "                  [119.015311, 34.405324],\n" +
//                "                  [118.994994, 34.404121],\n" +
//                "                  [118.995162, 34.39029],\n" +
//                "                  [118.980794, 34.379636],\n" +
//                "                  [118.974901, 34.36911],\n" +
//                "                  [118.96463, 34.36086],\n" +
//                "                  [118.963788, 34.350546],\n" +
//                "                  [118.95363, 34.350116],\n" +
//                "                  [118.953237, 34.368508],\n" +
//                "                  [118.939991, 34.382343],\n" +
//                "                  [118.931909, 34.374738],\n" +
//                "                  [118.924837, 34.379293],\n" +
//                "                  [118.913388, 34.378262],\n" +
//                "                  [118.907439, 34.368036],\n" +
//                "                  [118.874942, 34.367692],\n" +
//                "                  [118.864335, 34.365887],\n" +
//                "                  [118.837787, 34.368809],\n" +
//                "                  [118.825328, 34.358797],\n" +
//                "                  [118.812138, 34.357336],\n" +
//                "                  [118.810903, 34.343196],\n" +
//                "                  [118.78531, 34.311814],\n" +
//                "                  [118.766564, 34.308891],\n" +
//                "                  [118.758258, 34.312932],\n" +
//                "                  [118.759829, 34.321058],\n" +
//                "                  [118.748268, 34.330817],\n" +
//                "                  [118.739905, 34.325056],\n" +
//                "                  [118.708194, 34.336362],\n" +
//                "                  [118.693265, 34.345904],\n" +
//                "                  [118.676371, 34.345345],\n" +
//                "                  [118.676596, 34.339414],\n" +
//                "                  [118.6661, 34.342423],\n" +
//                "                  [118.644268, 34.359012],\n" +
//                "                  [118.635232, 34.355058],\n" +
//                "                  [118.633155, 34.344744],\n" +
//                "                  [118.621986, 34.341133],\n" +
//                "                  [118.631752, 34.337179],\n" +
//                "                  [118.634895, 34.320456],\n" +
//                "                  [118.632706, 34.308246],\n" +
//                "                  [118.624063, 34.305752],\n" +
//                "                  [118.593362, 34.308031],\n" +
//                "                  [118.571473, 34.298957],\n" +
//                "                  [118.57456, 34.278399],\n" +
//                "                  [118.566871, 34.274184],\n" +
//                "                  [118.56109, 34.247983],\n" +
//                "                  [118.565468, 34.236794],\n" +
//                "                  [118.573494, 34.233566],\n" +
//                "                  [118.559687, 34.187806],\n" +
//                "                  [118.560192, 34.165454],\n" +
//                "                  [118.519221, 34.168598],\n" +
//                "                  [118.505358, 34.158562],\n" +
//                "                  [118.50968, 34.148784],\n" +
//                "                  [118.503674, 34.137194],\n" +
//                "                  [118.503225, 34.123147],\n" +
//                "                  [118.517705, 34.117545],\n" +
//                "                  [118.489923, 34.106684],\n" +
//                "                  [118.482627, 34.100046],\n" +
//                "                  [118.469718, 34.097201],\n" +
//                "                  [118.451029, 34.106598],\n" +
//                "                  [118.43436, 34.105089],\n" +
//                "                  [118.428466, 34.108623],\n" +
//                "                  [118.382949, 34.117286],\n" +
//                "                  [118.363586, 34.118795],\n" +
//                "                  [118.306001, 34.11358],\n" +
//                "                  [118.292307, 34.109701],\n" +
//                "                  [118.28922, 34.102589],\n" +
//                "                  [118.232646, 34.100304],\n" +
//                "                  [118.222824, 34.106986],\n" +
//                "                  [118.213619, 34.127241],\n" +
//                "                  [118.213844, 34.14202],\n" +
//                "                  [118.187745, 34.161664],\n" +
//                "                  [118.177306, 34.156366],\n" +
//                "                  [118.15654, 34.161147],\n" +
//                "                  [118.150703, 34.158476],\n" +
//                "                  [118.154239, 34.148784],\n" +
//                "                  [118.150534, 34.137281],\n" +
//                "                  [118.139309, 34.136419],\n" +
//                "                  [118.120732, 34.144045],\n" +
//                "                  [118.10294, 34.139521],\n" +
//                "                  [118.097047, 34.150292],\n" +
//                "                  [118.088292, 34.15503],\n" +
//                "                  [118.083072, 34.165411],\n" +
//                "                  [118.072408, 34.170924],\n" +
//                "                  [118.060622, 34.157055],\n" +
//                "                  [118.064158, 34.152532],\n" +
//                "                  [118.055571, 34.148827],\n" +
//                "                  [117.998997, 34.140038],\n" +
//                "                  [117.994394, 34.123793],\n" +
//                "                  [117.996583, 34.105649],\n" +
//                "                  [118.007696, 34.100348],\n" +
//                "                  [118.005507, 34.09164],\n" +
//                "                  [118.013084, 34.083923],\n" +
//                "                  [118.048611, 34.092545],\n" +
//                "                  [118.063821, 34.053781],\n" +
//                "                  [118.059836, 34.048174],\n" +
//                "                  [118.063653, 34.020738],\n" +
//                "                  [118.054168, 34.009692],\n" +
//                "                  [118.049621, 34.01336],\n" +
//                "                  [118.034917, 34.01267],\n" +
//                "                  [118.028406, 33.998386],\n" +
//                "                  [118.03385, 33.983927],\n" +
//                "                  [118.046254, 33.98151],\n" +
//                "                  [118.053045, 33.971883],\n" +
//                "                  [118.072408, 33.965408],\n" +
//                "                  [118.085654, 33.965192],\n" +
//                "                  [118.085766, 33.942048],\n" +
//                "                  [118.100022, 33.941184],\n" +
//                "                  [118.106139, 33.935138],\n" +
//                "                  [118.113772, 33.936865],\n" +
//                "                  [118.117589, 33.954614],\n" +
//                "                  [118.125896, 33.954959],\n" +
//                "                  [118.141162, 33.934749],\n" +
//                "                  [118.149356, 33.901011],\n" +
//                "                  [118.156428, 33.889691],\n" +
//                "                  [118.168495, 33.881005],\n" +
//                "                  [118.174612, 33.865186],\n" +
//                "                  [118.176015, 33.844133],\n" +
//                "                  [118.184771, 33.844176],\n" +
//                "                  [118.18797, 33.822945],\n" +
//                "                  [118.183368, 33.816155],\n" +
//                "                  [118.169056, 33.820134],\n" +
//                "                  [118.168887, 33.807591],\n" +
//                "                  [118.159515, 33.795653],\n" +
//                "                  [118.170459, 33.786221],\n" +
//                "                  [118.178597, 33.763115],\n" +
//                "                  [118.170852, 33.759134],\n" +
//                "                  [118.168326, 33.749569],\n" +
//                "                  [118.185332, 33.744937],\n" +
//                "                  [118.177811, 33.736107],\n" +
//                "                  [118.160862, 33.735327],\n" +
//                "                  [118.153341, 33.720175],\n" +
//                "                  [118.167709, 33.714979],\n" +
//                "                  [118.164005, 33.692633],\n" +
//                "                  [118.168102, 33.663739],\n" +
//                "                  [118.120115, 33.621659],\n" +
//                "                  [118.112257, 33.617064],\n" +
//                "                  [118.117252, 33.591657],\n" +
//                "                  [118.108384, 33.530017],\n" +
//                "                  [118.099124, 33.52941],\n" +
//                "                  [118.107992, 33.520167],\n" +
//                "                  [118.106813, 33.503934],\n" +
//                "                  [118.108104, 33.475107],\n" +
//                "                  [118.05776, 33.491693],\n" +
//                "                  [118.050632, 33.49204],\n" +
//                "                  [118.04356, 33.473718],\n" +
//                "                  [118.028631, 33.458432],\n" +
//                "                  [118.023579, 33.440928],\n" +
//                "                  [118.021952, 33.421596],\n" +
//                "                  [118.016844, 33.406692],\n" +
//                "                  [118.028238, 33.390569],\n" +
//                "                  [118.029304, 33.372269],\n" +
//                "                  [118.0059, 33.352095],\n" +
//                "                  [117.995517, 33.347095],\n" +
//                "                  [117.971607, 33.349313],\n" +
//                "                  [117.975368, 33.335745],\n" +
//                "                  [117.99243, 33.333135],\n" +
//                "                  [117.985919, 33.32026],\n" +
//                "                  [117.983281, 33.296768],\n" +
//                "                  [117.974245, 33.279711],\n" +
//                "                  [117.939448, 33.262606],\n" +
//                "                  [117.948035, 33.252115],\n" +
//                "                  [117.938999, 33.234309],\n" +
//                "                  [117.942366, 33.225078],\n" +
//                "                  [117.953816, 33.223945],\n" +
//                "                  [117.977444, 33.226253],\n" +
//                "                  [117.993496, 33.211272],\n" +
//                "                  [117.993608, 33.20561],\n" +
//                "                  [117.980924, 33.200166],\n" +
//                "                  [117.986424, 33.193371],\n" +
//                "                  [117.989175, 33.179648],\n" +
//                "                  [118.003879, 33.177033],\n" +
//                "                  [118.008482, 33.16784],\n" +
//                "                  [118.017406, 33.164615],\n" +
//                "                  [118.025656, 33.155943],\n" +
//                "                  [118.036713, 33.1525],\n" +
//                "                  [118.038228, 33.135196],\n" +
//                "                  [118.046871, 33.138422],\n" +
//                "                  [118.069153, 33.139948],\n" +
//                "                  [118.088292, 33.149667],\n" +
//                "                  [118.11888, 33.160562],\n" +
//                "                  [118.149019, 33.169495],\n" +
//                "                  [118.159964, 33.181042],\n" +
//                "                  [118.156315, 33.195461],\n" +
//                "                  [118.168382, 33.21345],\n" +
//                "                  [118.178204, 33.217979],\n" +
//                "                  [118.194368, 33.211664],\n" +
//                "                  [118.211599, 33.19912],\n" +
//                "                  [118.221364, 33.180693],\n" +
//                "                  [118.261718, 33.199948],\n" +
//                "                  [118.320201, 33.196246],\n" +
//                "                  [118.424313, 33.187141],\n" +
//                "                  [118.443452, 33.204783],\n" +
//                "                  [118.511083, 33.263868],\n" +
//                "                  [118.667784, 33.311734],\n" +
//                "                  [118.703424, 33.323697],\n" +
//                "                  [118.780147, 33.345834],\n" +
//                "                  [118.746921, 33.386701],\n" +
//                "                  [118.782841, 33.444012],\n" +
//                "                  [118.779866, 33.487568],\n" +
//                "                  [118.785591, 33.493082],\n" +
//                "                  [118.789969, 33.508709],\n" +
//                "                  [118.789744, 33.5367],\n" +
//                "                  [118.797433, 33.560604],\n" +
//                "                  [118.803102, 33.561776],\n" +
//                "                  [118.806357, 33.572749],\n" +
//                "                  [118.78531, 33.587018],\n" +
//                "                  [118.770605, 33.60705],\n" +
//                "                  [118.757977, 33.635746],\n" +
//                "                  [118.799566, 33.64671],\n" +
//                "                  [118.792607, 33.666555],\n" +
//                "                  [118.798612, 33.69155],\n" +
//                "                  [118.813148, 33.700948],\n" +
//                "                  [118.809276, 33.715975],\n" +
//                "                  [118.814215, 33.730868],\n" +
//                "                  [118.80226, 33.741215],\n" +
//                "                  [118.805122, 33.763245],\n" +
//                "                  [118.79575, 33.772852],\n" +
//                "                  [118.805628, 33.794355],\n" +
//                "                  [118.799847, 33.813517],\n" +
//                "                  [118.807311, 33.819356],\n" +
//                "                  [118.818985, 33.844954],\n" +
//                "                  [118.828751, 33.858615],\n" +
//                "                  [118.857151, 33.880227],\n" +
//                "                  [118.863661, 33.891376],\n" +
//                "                  [118.874493, 33.892543],\n" +
//                "                  [118.885381, 33.885888],\n" +
//                "                  [118.907383, 33.889777],\n" +
//                "                  [118.922143, 33.902524],\n" +
//                "                  [118.933088, 33.900709],\n" +
//                "                  [118.941338, 33.907751],\n" +
//                "                  [118.948803, 33.906801],\n" +
//                "                  [118.950767, 33.914275],\n" +
//                "                  [118.972263, 33.919372],\n" +
//                "                  [118.987585, 33.904554],\n" +
//                "                  [118.999708, 33.9049],\n" +
//                "                  [118.997295, 33.897296],\n" +
//                "                  [119.008015, 33.896604],\n" +
//                "                  [119.024853, 33.911164],\n" +
//                "                  [119.035853, 33.928054],\n" +
//                "                  [119.044553, 33.929436],\n" +
//                "                  [119.053252, 33.937383],\n" +
//                "                  [119.050951, 33.957507],\n" +
//                "                  [119.040736, 33.959622],\n" +
//                "                  [119.0427, 33.968905],\n" +
//                "                  [119.055441, 33.973135],\n" +
//                "                  [119.058864, 33.978359],\n" +
//                "                  [119.07312, 33.98151],\n" +
//                "                  [119.081932, 33.987769],\n" +
//                "                  [119.08384, 34.001407],\n" +
//                "                  [119.114541, 34.024406],\n" +
//                "                  [119.129751, 34.028849],\n" +
//                "                  [119.148833, 34.045845],\n" +
//                "                  [119.155119, 34.056973],\n" +
//                "                  [119.15815, 34.076421],\n" +
//                "                  [119.166456, 34.088105],\n" +
//                "                  [119.174931, 34.093494]\n" +
//                "              ]\n" +
//                "          ]\n" +
//                "      ]\n" +
//                "  }";
//        GeometryJSON geometryJSON = new GeometryJSON();
//        Geometry geometry = geometryJSON.read(new StringReader(json));
//        Geometry point = geometryJSON.read(new StringReader("{\"type\": \"Point\", \"coordinates\": [118.275162, 33.963008]}"));
//        DruidPooledConnection connection = dataSource.getConnection();
//        PreparedStatement statement =
//                connection.prepareStatement(
//                        "INSERT INTO tb_city(cityid,cityname,geom,parentid,centerpoint) VALUES(?,?,ST_GeomFromText(?),?,ST_GeomFromText(?))");
//        //设置参数ST_GeomFromText('POINT(118.275162 33.963008)')
//        statement.setObject(1,"321400");
//        statement.setObject(2,"宿迁市");
//        statement.setObject(3,geometry.toString());
//        statement.setObject(4,"320000");
//        statement.setObject(5,point.toString());
//        int rows = statement.executeUpdate();
//        if (rows>0){
//            System.out.println("SUCCESS!");
//        }else {
//            System.out.println("FAILED!");
//        }
//
//    }
//
//    @Autowired
//    private StopService stopService;
//
//    @Test
//    public void stopService() throws ParseException, IOException, org.locationtech.jts.io.ParseException {
////        GeometryJSON geometryJSON = new GeometryJSON();
////        String json = "{\"type\":\"Polygon\",\"coordinates\":[[[120.177383,33.07225],[120.177941,33.073374],[120.179197,33.073203],[120.178649,33.072286],[120.177544,33.071549],[120.176965,33.071764],[120.177383,33.07225]]]}";
////        Geometry geometry = geometryJSON.read(new StringReader(json));
////        System.out.println(geometry);
////        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mmm:ss");
////        String format = simpleDateFormat.format(new Date());
////        Stop stop = new Stop();
////        stop.setStopno(UUID.randomUUID().toString());
////        stop.setStopname("停车点3号");
////        stop.setBuilddate(simpleDateFormat.parse(format));
////        stop.setCapacity(100);
////        stop.setSize(0);
////        stop.setBelong("321200");
////        stop.setGeom((Polygon) geometry);
////        Result rows = stopService.addStop(stop);
//////        System.out.println(rows);
////        List<Stop> stops = stopService.selectStopsByName_Belong(null, null);
////        stops.forEach(System.out::println);
////        List<HashMap<String, Object>> hashMaps = stopService.selectStopsByCityGroup();
////        hashMaps.forEach(System.out::println);
////        Stop stop = stopService.selectById(1);
////        System.out.println(stop);
////        Point centroid = stop.getGeom().getCentroid();
////        System.out.println(centroid);
////        StringWriter writer = new StringWriter();
////        GeometryJSON geometryJSON = new GeometryJSON();
////        geometryJSON.write(centroid,writer);
////        System.out.println(writer.toString());
////        Stop stop = new Stop();
////        stop.setId(1);
////        stop.setSize(20);
////        stop.setCapacity(50);
////        Result result = stopService.updateCapacityAndSizeById(stop);
////        System.out.println(result);
////        List<HashMap<String, Object>> mapList = stopService.selectStopsByCityGroup();
////        List<HashMap<String, Object>> belong = mapList.stream().filter(e -> "321200".equals(e.get("belong"))).collect(Collectors.toList());
////        System.out.println(belong);
//
////        List<Stop> stops = stopService.selectStopsByName_Belong("", "");
////        System.out.println(stops.size());
////        Stop stop = stops.get(0);
////        Polygon geom = stop.getGeom();
////        System.out.println(geom.getPrecisionModel());
////        GeometryFactory geometryFactory = new GeometryFactory();
////        WKTReader wktReader = new WKTReader(geometryFactory);
////        String polygon = "POLYGON((118.664 33.1115, 118.677 33.1198, 118.686 33.1125, 118.671 33.1048, 118.664 33.1115))";
////        Geometry geometry = wktReader.read(polygon);
////        System.out.println(geometry);
////        Geometry envelope = geometry.getEnvelope();
////        System.out.println(envelope);
////        Envelope envelopeInternal = geometry.getEnvelopeInternal();
////        System.out.println(envelopeInternal);
//
////        GeometryJSON geometryJSON = new GeometryJSON();
////        StringWriter writer = new StringWriter();
////
////
////        Geometry geometry = geometryJSON.read(new StringReader(polygon));
////        System.out.println(geometry.getArea());
//
//    }
//
//
//    @Autowired
//    private BikeService bikeService;
//    @Autowired
//    private BikeMapper bikeMapper;
//    @Test
//    public void bikeService_test() throws IOException {
// /*       List<Bike> bikes = bikeMapper.selectBike("","");
////        bikes.forEach(System.out::println);
//        HashMap<String,Object> featureCollection = new HashMap<>();
//        featureCollection.put("type","FeatureCollection");
//        //构造List集合-转为JSON-添加到hashMap集合中
//        List<Map<String,Object>> features = new ArrayList<>();
//        bikes.forEach(e->{
//            HashMap<String,Object> feature = new HashMap<>();
//            feature.put("type","feature");
//            HashMap<String,Object> properties = new HashMap<>();
//            //添加属性信息
//            properties.put("id",e.getId());
//            properties.put("bno",e.getBno());
//            properties.put("status",e.getStatus());
//            properties.put("startdate",e.getStartdate());
//            properties.put("type",e.getBikeType().getType());
//            properties.put("stopname",e.getStop().getStopname());
//            feature.put("properties",properties);
//            //添加几何信息
//            feature.put("geometry",e.getLocation());
//            //添加到父集合中
//            features.add(feature);
//        });
//        //添加feature集合到FeatureCollection中
//        featureCollection.put("feature",features);
//        System.out.println(featureCollection);*/
//
////        GeometryJSON geometryJSON = new GeometryJSON();
////        String point = "{\"type\":\"Point\",\"coordinates\":[118.6746,33.1122]}";
////        Geometry geometry = geometryJSON.read(new StringReader(point));
////
////        Bike bike = new Bike();
////        bike.setLocation(geometry.toString());
////        bike.setLocation("POINT (119.5513234865979 34.57890538689277)");
////        bike.setStopid(1);
////        bike.setTypeid(1);
////        bike.setStatus(0);
////        bike.setBno(UUID.randomUUID().toString());
////
////        String json = "{\"type\": \"Point\", \"coordinates\": [121.12604564501704, 31.949393616562343]}";
//
//
//
////        System.out.println(bike);
////        Result result = bikeService.insertBike(bike);
////        System.out.println(result);
////        long start = System.currentTimeMillis();
////        for (int i = 0; i < 1000; i++) {
////            bikeService.insertBike(bike);
////        }
////        long end = System.currentTimeMillis();//6403
////        System.out.println(end-start);
////
////
////        List<Bike> bikes = new ArrayList<>();
////        for (int i = 0; i < 10; i++) {
////            bike.setBno(UUID.randomUUID().toString());
//////            System.out.println(bike.toString());
////            bikes.add(bike);
////        }
////
////        bikes.forEach(System.out::println);
//
//        //批处理操作
////        long start = System.currentTimeMillis();
////        Result result = bikeService.batchInsertBike(bikes);
////        System.out.println(result);
////        long end = System.currentTimeMillis();//708
////        System.out.println(end-start);
//        Result result = bikeService.selectBikeAsList("", "");
//        List<HashMap<String,Object>> bikes = (List<HashMap<String, Object>>) result.getData();
//        bikes.forEach(System.out::println);
//
//    }
//
//
//    static Random random = new Random();
//
//    @Test
//    public void getRandom() throws IOException {
////        System.out.println(getDouble());
////        GeometryJSON geometryJSON = new GeometryJSON();
////        String point = "{\"type\":\"Point\",\"coordinates\":[118.6746,33.1122]}";
////        Geometry geometry = geometryJSON.read(new StringReader(point));
////        System.out.println(geometry.getCoordinate().getX());//经度
////        System.out.println(geometry.getCoordinate().getY());//纬度
////        System.out.println(geometry.getCoordinate());
////        System.out.println(geometry.getPrecisionModel());
////        System.out.println(geometry.getSRID());
////        geometry.getCoordinate().setX(getDouble()+geometry.getCoordinate().getX());
////        geometry.getCoordinate().setY(getDouble()+geometry.getCoordinate().getY());
////        System.out.println(geometry);
////        int count_ji=0;
////        int count_ou=0;
//        for (int i = 0; i < 10000; i++) {
////           if (random.nextInt()%2==0){
////               count_ou++;
////           }else {
////               count_ji++;
////           }
//            System.out.println(getDouble());
//        }
////        System.out.println("count_ji="+count_ji);
////        System.out.println("count_ou="+count_ou);
//
//    }
//
//    public static double getDouble(){
//        double nextDouble = random.nextDouble()/100.0;
//        if (nextDouble<0.003)
//            return nextDouble;
//        return getDouble();
//    }
//
//
//    @Autowired
//    private StopStoreService stopStoreService;
//
//    @Test
//    public void StopStoreService_test(){
////        StopStore stopStore = new StopStore();
////        stopStore.setStopid(2);
////        stopStore.setTypeid(6);
////        stopStore.setAddtion(52);
////        stopStore.setReduce(0);
////        stopStore.setUsername("admin2");
////        Result result = stopStoreService.addStopStore("admin2", stopStore);
////        System.out.println(result);
//        Result stopStores = stopStoreService.selectAllDeatil();
//        System.out.println(stopStores);
////        stopStores.forEach(System.out::println);
//    }
//
//
//    @Test
//    public void json_test() throws IOException {
//        GeometryFactory geometryFactory = new GeometryFactory();
//        ObjectMapper objectMapper = new ObjectMapper();
//        String json = "{\n" +
//                "      \"type\": \"MultiPolygon\",\n" +
//                "      \"coordinates\": [\n" +
//                "          [\n" +
//                "              [\n" +
//                "                  [119.174931, 34.093494],\n" +
//                "                  [119.166849, 34.10746],\n" +
//                "                  [119.176166, 34.118579],\n" +
//                "                  [119.168421, 34.129654],\n" +
//                "                  [119.15641, 34.133618],\n" +
//                "                  [119.150348, 34.140555],\n" +
//                "                  [119.142828, 34.134049],\n" +
//                "                  [119.129077, 34.145768],\n" +
//                "                  [119.145409, 34.158692],\n" +
//                "                  [119.141874, 34.161578],\n" +
//                "                  [119.144287, 34.176265],\n" +
//                "                  [119.136991, 34.177858],\n" +
//                "                  [119.13295, 34.205415],\n" +
//                "                  [119.099667, 34.208042],\n" +
//                "                  [119.09703, 34.232921],\n" +
//                "                  [119.09108, 34.23688],\n" +
//                "                  [119.091866, 34.262353],\n" +
//                "                  [119.074074, 34.265107],\n" +
//                "                  [119.082774, 34.310868],\n" +
//                "                  [119.073008, 34.331763],\n" +
//                "                  [119.075477, 34.342681],\n" +
//                "                  [119.062232, 34.338683],\n" +
//                "                  [119.057068, 34.356176],\n" +
//                "                  [119.056227, 34.3823],\n" +
//                "                  [119.065263, 34.409232],\n" +
//                "                  [119.086871, 34.409747],\n" +
//                "                  [119.089565, 34.42087],\n" +
//                "                  [119.061222, 34.420183],\n" +
//                "                  [119.041409, 34.421901],\n" +
//                "                  [119.038659, 34.403735],\n" +
//                "                  [119.033215, 34.398881],\n" +
//                "                  [119.015311, 34.405324],\n" +
//                "                  [118.994994, 34.404121],\n" +
//                "                  [118.995162, 34.39029],\n" +
//                "                  [118.980794, 34.379636],\n" +
//                "                  [118.974901, 34.36911],\n" +
//                "                  [118.96463, 34.36086],\n" +
//                "                  [118.963788, 34.350546],\n" +
//                "                  [118.95363, 34.350116],\n" +
//                "                  [118.953237, 34.368508],\n" +
//                "                  [118.939991, 34.382343],\n" +
//                "                  [118.931909, 34.374738],\n" +
//                "                  [118.924837, 34.379293],\n" +
//                "                  [118.913388, 34.378262],\n" +
//                "                  [118.907439, 34.368036],\n" +
//                "                  [118.874942, 34.367692],\n" +
//                "                  [118.864335, 34.365887],\n" +
//                "                  [118.837787, 34.368809],\n" +
//                "                  [118.825328, 34.358797],\n" +
//                "                  [118.812138, 34.357336],\n" +
//                "                  [118.810903, 34.343196],\n" +
//                "                  [118.78531, 34.311814],\n" +
//                "                  [118.766564, 34.308891],\n" +
//                "                  [118.758258, 34.312932],\n" +
//                "                  [118.759829, 34.321058],\n" +
//                "                  [118.748268, 34.330817],\n" +
//                "                  [118.739905, 34.325056],\n" +
//                "                  [118.708194, 34.336362],\n" +
//                "                  [118.693265, 34.345904],\n" +
//                "                  [118.676371, 34.345345],\n" +
//                "                  [118.676596, 34.339414],\n" +
//                "                  [118.6661, 34.342423],\n" +
//                "                  [118.644268, 34.359012],\n" +
//                "                  [118.635232, 34.355058],\n" +
//                "                  [118.633155, 34.344744],\n" +
//                "                  [118.621986, 34.341133],\n" +
//                "                  [118.631752, 34.337179],\n" +
//                "                  [118.634895, 34.320456],\n" +
//                "                  [118.632706, 34.308246],\n" +
//                "                  [118.624063, 34.305752],\n" +
//                "                  [118.593362, 34.308031],\n" +
//                "                  [118.571473, 34.298957],\n" +
//                "                  [118.57456, 34.278399],\n" +
//                "                  [118.566871, 34.274184],\n" +
//                "                  [118.56109, 34.247983],\n" +
//                "                  [118.565468, 34.236794],\n" +
//                "                  [118.573494, 34.233566],\n" +
//                "                  [118.559687, 34.187806],\n" +
//                "                  [118.560192, 34.165454],\n" +
//                "                  [118.519221, 34.168598],\n" +
//                "                  [118.505358, 34.158562],\n" +
//                "                  [118.50968, 34.148784],\n" +
//                "                  [118.503674, 34.137194],\n" +
//                "                  [118.503225, 34.123147],\n" +
//                "                  [118.517705, 34.117545],\n" +
//                "                  [118.489923, 34.106684],\n" +
//                "                  [118.482627, 34.100046],\n" +
//                "                  [118.469718, 34.097201],\n" +
//                "                  [118.451029, 34.106598],\n" +
//                "                  [118.43436, 34.105089],\n" +
//                "                  [118.428466, 34.108623],\n" +
//                "                  [118.382949, 34.117286],\n" +
//                "                  [118.363586, 34.118795],\n" +
//                "                  [118.306001, 34.11358],\n" +
//                "                  [118.292307, 34.109701],\n" +
//                "                  [118.28922, 34.102589],\n" +
//                "                  [118.232646, 34.100304],\n" +
//                "                  [118.222824, 34.106986],\n" +
//                "                  [118.213619, 34.127241],\n" +
//                "                  [118.213844, 34.14202],\n" +
//                "                  [118.187745, 34.161664],\n" +
//                "                  [118.177306, 34.156366],\n" +
//                "                  [118.15654, 34.161147],\n" +
//                "                  [118.150703, 34.158476],\n" +
//                "                  [118.154239, 34.148784],\n" +
//                "                  [118.150534, 34.137281],\n" +
//                "                  [118.139309, 34.136419],\n" +
//                "                  [118.120732, 34.144045],\n" +
//                "                  [118.10294, 34.139521],\n" +
//                "                  [118.097047, 34.150292],\n" +
//                "                  [118.088292, 34.15503],\n" +
//                "                  [118.083072, 34.165411],\n" +
//                "                  [118.072408, 34.170924],\n" +
//                "                  [118.060622, 34.157055],\n" +
//                "                  [118.064158, 34.152532],\n" +
//                "                  [118.055571, 34.148827],\n" +
//                "                  [117.998997, 34.140038],\n" +
//                "                  [117.994394, 34.123793],\n" +
//                "                  [117.996583, 34.105649],\n" +
//                "                  [118.007696, 34.100348],\n" +
//                "                  [118.005507, 34.09164],\n" +
//                "                  [118.013084, 34.083923],\n" +
//                "                  [118.048611, 34.092545],\n" +
//                "                  [118.063821, 34.053781],\n" +
//                "                  [118.059836, 34.048174],\n" +
//                "                  [118.063653, 34.020738],\n" +
//                "                  [118.054168, 34.009692],\n" +
//                "                  [118.049621, 34.01336],\n" +
//                "                  [118.034917, 34.01267],\n" +
//                "                  [118.028406, 33.998386],\n" +
//                "                  [118.03385, 33.983927],\n" +
//                "                  [118.046254, 33.98151],\n" +
//                "                  [118.053045, 33.971883],\n" +
//                "                  [118.072408, 33.965408],\n" +
//                "                  [118.085654, 33.965192],\n" +
//                "                  [118.085766, 33.942048],\n" +
//                "                  [118.100022, 33.941184],\n" +
//                "                  [118.106139, 33.935138],\n" +
//                "                  [118.113772, 33.936865],\n" +
//                "                  [118.117589, 33.954614],\n" +
//                "                  [118.125896, 33.954959],\n" +
//                "                  [118.141162, 33.934749],\n" +
//                "                  [118.149356, 33.901011],\n" +
//                "                  [118.156428, 33.889691],\n" +
//                "                  [118.168495, 33.881005],\n" +
//                "                  [118.174612, 33.865186],\n" +
//                "                  [118.176015, 33.844133],\n" +
//                "                  [118.184771, 33.844176],\n" +
//                "                  [118.18797, 33.822945],\n" +
//                "                  [118.183368, 33.816155],\n" +
//                "                  [118.169056, 33.820134],\n" +
//                "                  [118.168887, 33.807591],\n" +
//                "                  [118.159515, 33.795653],\n" +
//                "                  [118.170459, 33.786221],\n" +
//                "                  [118.178597, 33.763115],\n" +
//                "                  [118.170852, 33.759134],\n" +
//                "                  [118.168326, 33.749569],\n" +
//                "                  [118.185332, 33.744937],\n" +
//                "                  [118.177811, 33.736107],\n" +
//                "                  [118.160862, 33.735327],\n" +
//                "                  [118.153341, 33.720175],\n" +
//                "                  [118.167709, 33.714979],\n" +
//                "                  [118.164005, 33.692633],\n" +
//                "                  [118.168102, 33.663739],\n" +
//                "                  [118.120115, 33.621659],\n" +
//                "                  [118.112257, 33.617064],\n" +
//                "                  [118.117252, 33.591657],\n" +
//                "                  [118.108384, 33.530017],\n" +
//                "                  [118.099124, 33.52941],\n" +
//                "                  [118.107992, 33.520167],\n" +
//                "                  [118.106813, 33.503934],\n" +
//                "                  [118.108104, 33.475107],\n" +
//                "                  [118.05776, 33.491693],\n" +
//                "                  [118.050632, 33.49204],\n" +
//                "                  [118.04356, 33.473718],\n" +
//                "                  [118.028631, 33.458432],\n" +
//                "                  [118.023579, 33.440928],\n" +
//                "                  [118.021952, 33.421596],\n" +
//                "                  [118.016844, 33.406692],\n" +
//                "                  [118.028238, 33.390569],\n" +
//                "                  [118.029304, 33.372269],\n" +
//                "                  [118.0059, 33.352095],\n" +
//                "                  [117.995517, 33.347095],\n" +
//                "                  [117.971607, 33.349313],\n" +
//                "                  [117.975368, 33.335745],\n" +
//                "                  [117.99243, 33.333135],\n" +
//                "                  [117.985919, 33.32026],\n" +
//                "                  [117.983281, 33.296768],\n" +
//                "                  [117.974245, 33.279711],\n" +
//                "                  [117.939448, 33.262606],\n" +
//                "                  [117.948035, 33.252115],\n" +
//                "                  [117.938999, 33.234309],\n" +
//                "                  [117.942366, 33.225078],\n" +
//                "                  [117.953816, 33.223945],\n" +
//                "                  [117.977444, 33.226253],\n" +
//                "                  [117.993496, 33.211272],\n" +
//                "                  [117.993608, 33.20561],\n" +
//                "                  [117.980924, 33.200166],\n" +
//                "                  [117.986424, 33.193371],\n" +
//                "                  [117.989175, 33.179648],\n" +
//                "                  [118.003879, 33.177033],\n" +
//                "                  [118.008482, 33.16784],\n" +
//                "                  [118.017406, 33.164615],\n" +
//                "                  [118.025656, 33.155943],\n" +
//                "                  [118.036713, 33.1525],\n" +
//                "                  [118.038228, 33.135196],\n" +
//                "                  [118.046871, 33.138422],\n" +
//                "                  [118.069153, 33.139948],\n" +
//                "                  [118.088292, 33.149667],\n" +
//                "                  [118.11888, 33.160562],\n" +
//                "                  [118.149019, 33.169495],\n" +
//                "                  [118.159964, 33.181042],\n" +
//                "                  [118.156315, 33.195461],\n" +
//                "                  [118.168382, 33.21345],\n" +
//                "                  [118.178204, 33.217979],\n" +
//                "                  [118.194368, 33.211664],\n" +
//                "                  [118.211599, 33.19912],\n" +
//                "                  [118.221364, 33.180693],\n" +
//                "                  [118.261718, 33.199948],\n" +
//                "                  [118.320201, 33.196246],\n" +
//                "                  [118.424313, 33.187141],\n" +
//                "                  [118.443452, 33.204783],\n" +
//                "                  [118.511083, 33.263868],\n" +
//                "                  [118.667784, 33.311734],\n" +
//                "                  [118.703424, 33.323697],\n" +
//                "                  [118.780147, 33.345834],\n" +
//                "                  [118.746921, 33.386701],\n" +
//                "                  [118.782841, 33.444012],\n" +
//                "                  [118.779866, 33.487568],\n" +
//                "                  [118.785591, 33.493082],\n" +
//                "                  [118.789969, 33.508709],\n" +
//                "                  [118.789744, 33.5367],\n" +
//                "                  [118.797433, 33.560604],\n" +
//                "                  [118.803102, 33.561776],\n" +
//                "                  [118.806357, 33.572749],\n" +
//                "                  [118.78531, 33.587018],\n" +
//                "                  [118.770605, 33.60705],\n" +
//                "                  [118.757977, 33.635746],\n" +
//                "                  [118.799566, 33.64671],\n" +
//                "                  [118.792607, 33.666555],\n" +
//                "                  [118.798612, 33.69155],\n" +
//                "                  [118.813148, 33.700948],\n" +
//                "                  [118.809276, 33.715975],\n" +
//                "                  [118.814215, 33.730868],\n" +
//                "                  [118.80226, 33.741215],\n" +
//                "                  [118.805122, 33.763245],\n" +
//                "                  [118.79575, 33.772852],\n" +
//                "                  [118.805628, 33.794355],\n" +
//                "                  [118.799847, 33.813517],\n" +
//                "                  [118.807311, 33.819356],\n" +
//                "                  [118.818985, 33.844954],\n" +
//                "                  [118.828751, 33.858615],\n" +
//                "                  [118.857151, 33.880227],\n" +
//                "                  [118.863661, 33.891376],\n" +
//                "                  [118.874493, 33.892543],\n" +
//                "                  [118.885381, 33.885888],\n" +
//                "                  [118.907383, 33.889777],\n" +
//                "                  [118.922143, 33.902524],\n" +
//                "                  [118.933088, 33.900709],\n" +
//                "                  [118.941338, 33.907751],\n" +
//                "                  [118.948803, 33.906801],\n" +
//                "                  [118.950767, 33.914275],\n" +
//                "                  [118.972263, 33.919372],\n" +
//                "                  [118.987585, 33.904554],\n" +
//                "                  [118.999708, 33.9049],\n" +
//                "                  [118.997295, 33.897296],\n" +
//                "                  [119.008015, 33.896604],\n" +
//                "                  [119.024853, 33.911164],\n" +
//                "                  [119.035853, 33.928054],\n" +
//                "                  [119.044553, 33.929436],\n" +
//                "                  [119.053252, 33.937383],\n" +
//                "                  [119.050951, 33.957507],\n" +
//                "                  [119.040736, 33.959622],\n" +
//                "                  [119.0427, 33.968905],\n" +
//                "                  [119.055441, 33.973135],\n" +
//                "                  [119.058864, 33.978359],\n" +
//                "                  [119.07312, 33.98151],\n" +
//                "                  [119.081932, 33.987769],\n" +
//                "                  [119.08384, 34.001407],\n" +
//                "                  [119.114541, 34.024406],\n" +
//                "                  [119.129751, 34.028849],\n" +
//                "                  [119.148833, 34.045845],\n" +
//                "                  [119.155119, 34.056973],\n" +
//                "                  [119.15815, 34.076421],\n" +
//                "                  [119.166456, 34.088105],\n" +
//                "                  [119.174931, 34.093494]\n" +
//                "              ]\n" +
//                "          ]\n" +
//                "      ]\n" +
//                "  }";
//       GeometryJSON geometryJSON = new GeometryJSON();
//        Geometry geometry = geometryJSON.read(new StringReader(json));
//        System.out.println(geometry);
//
//    }
//
//
//    @Test
//    public void md5(){
//        String s = DigestUtils.md5DigestAsHex("d41d8cd98f00b204e9800998ecf8427e".getBytes());
//        System.out.println(s);
//    }
//
//
//    @Test
//    public void geojson_test() throws IOException {
//        String geojson = "{\"type\":\"Polygon\",\"coordinates\":[[[119.273071,33.093843],[119.273071,33.254767],[119.775696,33.254767],[119.775696,33.093843],[119.273071,33.093843]]]}";
//        //GeometryJSON:Reads and writes geometry objects to and from geojson
//        GeometryJSON geometryJSON = new GeometryJSON();
//        Geometry geometry = geometryJSON.read(new StringReader(geojson));
//        System.out.println(geometry);
//    }
//
//    @Autowired
//    private CityService cityService;
//
//    @Test
//    public void cityService_Test(){
//        City city = cityService.selectOneByCityName("无锡市");
//        System.out.println(city);
//    }
//
//
//    @Test
//    public void cityService() throws IOException {
//        String geojson = "{\"type\":\"Polygon\",\"coordinates\":[[[119.757843,32.719132],[119.757843,32.803436],[119.995422,32.803436],[119.995422,32.719132],[119.757843,32.719132]]]}";
//        //GeometryJSON:Reads and writes geometry objects to and from geojson
//        GeometryJSON geometryJSON = new GeometryJSON();
//        Geometry geometry = geometryJSON.read(new StringReader(geojson));
////        System.out.println(geometry.toString());
////        List<City> cities = cityService.selectAll();
////        cities.forEach(e->{
////            MultiPolygon geom = e.getGeom();
////            System.out.println(geom.intersects(geometry)+e.getCityname());
////        });
//        //计算几何中心
//        Point centroid = geometry.getCentroid();
//        System.out.println(centroid);
//
//    }
//
//    @Test
//    public void test1() throws IOException {
////        String json = "MULTIPOLYGON (((120.553141 32.021143, 120.558529 32.03372, 120.547922 32.041046, 120.550503 32.050929, 120.5294 32.056444, 120.518456 32.065885, 120.512843 32.091732, 120.486745 32.099141, 120.482255 32.102933, 120.440779 32.119865, 120.435166 32.123921, 120.417038 32.124847, 120.387909 32.129696, 120.374383 32.128065, 120.356198 32.130754, 120.351034 32.149487, 120.347442 32.150545, 120.343233 32.170067, 120.371632 32.217555, 120.364617 32.237812, 120.35704 32.238736, 120.345927 32.249919, 120.350866 32.255995, 120.347218 32.266515, 120.352381 32.278663, 120.352325 32.30445, 120.365234 32.308806, 120.354234 32.324951, 120.353111 32.335947, 120.346208 32.343996, 120.359734 32.35798, 120.352381 32.368929, 120.351652 32.377898, 120.323028 32.368445, 120.310119 32.361542, 120.302542 32.37249, 120.292608 32.369544, 120.284526 32.379876, 120.289185 32.384975, 120.279924 32.421454, 120.280654 32.430989, 120.275153 32.438897, 120.271393 32.47228, 120.258316 32.498934, 120.270102 32.501788, 120.263704 32.514562, 120.270102 32.548707, 120.262301 32.562001, 120.262862 32.574855, 120.255734 32.597619, 120.23059 32.58683, 120.224584 32.58183, 120.207635 32.591567, 120.202303 32.600908, 120.196017 32.603013, 120.197251 32.619281, 120.17598 32.629102, 120.176317 32.62121, 120.154372 32.616037, 120.140846 32.6163, 120.122661 32.610293, 120.113176 32.613801, 120.109079 32.630505, 120.120977 32.633267, 120.121651 32.679197, 120.129508 32.689055, 120.138096 32.687171, 120.145055 32.705877, 120.157571 32.703249, 120.155382 32.714199, 120.164811 32.720331, 120.17323 32.715688, 120.181705 32.723177, 120.180863 32.741832, 120.196129 32.739336, 120.202583 32.74297, 120.206624 32.756586, 120.21465 32.767442, 120.228176 32.77856, 120.222396 32.792301, 120.199328 32.786962, 120.197027 32.781492, 120.18294 32.772913, 120.174184 32.779654, 120.162454 32.780967, 120.159255 32.787881, 120.168179 32.793045, 120.161107 32.809935, 120.167225 32.81501, 120.187037 32.812166, 120.193379 32.826078, 120.181031 32.82529, 120.184848 32.834126, 120.194894 32.842786, 120.195231 32.851358, 120.205895 32.852189, 120.20842 32.864345, 120.198767 32.866925, 120.200226 32.874488, 120.177046 32.877767, 120.181873 32.903907, 120.177495 32.909064, 120.176373 32.932529, 120.201068 32.930519, 120.204997 32.9206, 120.220431 32.917017, 120.238447 32.921168, 120.23884 32.927985, 120.280766 32.924926, 120.291991 32.933621, 120.295078 32.94546, 120.286378 32.949392, 120.287389 32.955202, 120.300353 32.954153, 120.299063 32.966601, 120.312364 32.992583, 120.308884 33.024407, 120.313038 33.048059, 120.30939 33.062283, 120.291991 33.081128, 120.293675 33.096698, 120.28969 33.104242, 120.296761 33.129573, 120.214145 33.13742, 120.168852 33.15311, 120.153418 33.157207, 120.094093 33.178645, 120.080848 33.181782, 120.048183 33.164049, 120.023993 33.158035, 120.003451 33.15603, 119.988073 33.151585, 119.968373 33.149885, 119.936438 33.155377, 119.916569 33.149449, 119.8944 33.151062, 119.890247 33.144306, 119.877282 33.146834, 119.867572 33.143391, 119.86847 33.154461, 119.843494 33.153459, 119.787313 33.13912, 119.772272 33.130924, 119.74926 33.131012, 119.739775 33.147401, 119.753077 33.16065, 119.751281 33.170803, 119.730346 33.172851, 119.728775 33.188709, 119.733489 33.205349, 119.731469 33.211664, 119.712611 33.219068, 119.708177 33.198336, 119.701835 33.201821, 119.692069 33.186923, 119.657271 33.162349, 119.653679 33.156553, 119.646158 33.130096, 119.649245 33.120244, 119.640321 33.119241, 119.64773 33.104111, 119.669731 33.089807, 119.683931 33.072535, 119.696615 33.016899, 119.708008 33.005681, 119.706661 32.989614, 119.696727 32.961316, 119.703069 32.944893, 119.714743 32.936374, 119.712386 32.921955, 119.719514 32.906179, 119.72507 32.901721, 119.746173 32.898706, 119.761888 32.904082, 119.775302 32.915968, 119.782599 32.914832, 119.789334 32.881177, 119.802691 32.862727, 119.79736 32.857086, 119.805778 32.851926, 119.795956 32.841474, 119.801513 32.837669, 119.800895 32.828265, 119.790849 32.820041, 119.78866 32.810197, 119.794666 32.803853, 119.806508 32.800572, 119.82357 32.807966, 119.830249 32.806872, 119.824917 32.794927, 119.833504 32.781973, 119.863082 32.775715, 119.866899 32.768493, 119.84529 32.752952, 119.843663 32.736709, 119.858031 32.721163, 119.85848 32.710608, 119.877282 32.69843, 119.870659 32.679066, 119.883512 32.673895, 119.894849 32.674334, 119.899171 32.65698, 119.89715 32.646242, 119.911855 32.625156, 119.90063 32.623973, 119.898666 32.616519, 119.890527 32.620334, 119.869537 32.619369, 119.866618 32.627611, 119.855056 32.631908, 119.844617 32.640368, 119.832999 32.640894, 119.828734 32.629365, 119.814365 32.627085, 119.816105 32.614897, 119.821774 32.607267, 119.815769 32.605644, 119.820034 32.592751, 119.815656 32.578759, 119.827162 32.55915, 119.835525 32.54976, 119.823402 32.535367, 119.824524 32.4963, 119.818182 32.491645, 119.814534 32.468855, 119.829688 32.460334, 119.84052 32.464463, 119.853372 32.46275, 119.859266 32.444477, 119.868526 32.438722, 119.863812 32.417104, 119.851745 32.411171, 119.853878 32.401854, 119.844561 32.395964, 119.856179 32.379744, 119.853709 32.361498, 119.842709 32.360662, 119.847311 32.328426, 119.843887 32.321872, 119.819192 32.302514, 119.834571 32.295694, 119.852699 32.28192, 119.869537 32.262686, 119.87846 32.247542, 119.891987 32.201083, 119.894063 32.182889, 119.930601 32.078942, 119.948729 32.047488, 119.975501 32.006886, 119.980103 31.998057, 120.016248 31.970461, 120.022422 31.967767, 120.064908 31.95549, 120.134784 31.939367, 120.17525 31.933845, 120.205895 31.931504, 120.236595 31.932918, 120.262974 31.941841, 120.353055 31.980617, 120.370678 31.990817, 120.387853 32.006665, 120.403736 32.016199, 120.465642 32.045811, 120.503807 32.041002, 120.52463 32.030455, 120.553141 32.021143)))";
////        ObjectMapper objectMapper = new ObjectMapper();
//        List<City> cities = cityMapper.selectAll();
//        cities.forEach(System.out::println);
//
//    }
//
//
//    @Autowired
//    private BikeTypeService bikeTypeService;
//
//    @Test
//    public void BikeTypeService_test() throws Exception {
//        //1-查询
////        Result result = bikeTypeService.selectBikeType();
////        System.out.println(result);
////        List<BikeType> bikeTypes = (List<BikeType>) result.getData();
////        bikeTypes.forEach(System.out::println);
//
//        //2-增加
////        BikeType bikeType = new BikeType();
////        bikeType.setId(null);
////        bikeType.setDescription("小黄车-价格优惠、非常实用的共享单车,舒适度强,出行的好伙伴,生活的好朋友!");
////        bikeType.setLimits(6F);
////        bikeType.setPrice(new BigDecimal(600));
////        bikeType.setType("小黄车");
////        bikeType.setPhoto(null);
////        Result result = bikeTypeService.insertBikeType(bikeType);
////        System.out.println(result);
//
//        //3-更新
////        File file = new File("C:\\Users\\13241\\Desktop\\小黄车.png");
////        FileInputStream fileInputStream = new FileInputStream(file);
////        ByteArrayOutputStream byteArrayInputStream = new ByteArrayOutputStream();
////        byte[] buffer = new byte[1024];
////        int len =-1;
////        while ((len=fileInputStream.read(buffer))!=-1){
////            byteArrayInputStream.write(buffer,0,len);
////        }
////        byte[] bytes = byteArrayInputStream.toByteArray();
////        BikeType bikeType = new BikeType();
////        bikeType.setId(2);
////        bikeType.setPhoto(bytes);
////        Result result = bikeTypeService.updateBikeType(2, bikeType);
////        System.out.println(result);
//
//        //查询
//        Result result = bikeTypeService.selectBikeTypeWithCount();
//        List<Map<String,Object>> maps = (List<Map<String, Object>>) result.getData();
//        maps.forEach(System.out::println);
//
//    }
//
//    @Autowired
//    private BikeStatusService bikeStatusService;
//
//    @Test
//    public void BikeStatusService_test(){
//        Result result = bikeStatusService.selectAllBikeStatus();
//        List<BikeStatus> bikeStatuses = (List<BikeStatus>) result.getData();
//        bikeStatuses.forEach(System.out::println);
//    }


}