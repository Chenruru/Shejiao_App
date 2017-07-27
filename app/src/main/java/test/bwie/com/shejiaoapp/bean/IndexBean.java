package test.bwie.com.shejiaoapp.bean;



import java.util.List;




/**
 * Created by muhanxi on 17/7/8.
 */
public class IndexBean {


    /**
     * pageCount : 共20条记录
     * data : [{"area":"安徽省-安庆市-枞阳县","picWidth":510,"createtime":1499485141560,"picHeight":507,"gender":"男","lng":0,"introduce":"hhaha","imagePath":"http://dyt-pict.oss-cn-beijing.aliyuncs.com/dliao/default_man.jpg","userId":23,"yxpassword":"U383lo04","password":"","lasttime":1499485141560,"phone":"18256127721","nickname":"安徽","age":"18","lat":0},{"area":"安徽省 安庆市 宿松县","picWidth":510,"createtime":1499485430311,"picHeight":507,"gender":"男","lng":116.293678,"introduce":"吐","imagePath":"http://dyt-pict.oss-cn-beijing.aliyuncs.com/dliao/default_man.jpg","userId":26,"yxpassword":"794o0802","password":"","lasttime":1499485430311,"phone":"18701366093","nickname":"看看","age":"37","lat":40.039186},{"area":"安徽省-安庆市-枞阳县","picWidth":700,"createtime":1499485210091,"picHeight":742,"gender":"女","lng":0,"introduce":"你今","imagePath":"http://dyt-pict.oss-cn-beijing.aliyuncs.com/dliao/default_woman.jpg","userId":25,"yxpassword":"j01m3731","password":"","lasttime":1499485210091,"phone":"15010082416","nickname":"呃呃呃","age":"24","lat":0},{"area":"重庆市-重庆市-巴南区","picWidth":510,"createtime":1499485591490,"picHeight":507,"gender":"男","lng":0,"introduce":"chdhc","imagePath":"http://dyt-pict.oss-cn-beijing.aliyuncs.com/dliao/default_man.jpg","userId":28,"yxpassword":"m190y2Ho","password":"","lasttime":1499485591490,"phone":"13569598678","nickname":"55675787","age":"24","lat":0},{"area":"山东省\t滨州市\t256600","picWidth":510,"createtime":1499485701312,"picHeight":507,"gender":"男","lng":116.293807,"introduce":"33","imagePath":"http://dyt-pict.oss-cn-beijing.aliyuncs.com/dliao/default_man.jpg","userId":29,"yxpassword":"5ru60T80","password":"","lasttime":1499485701312,"phone":"15210030000","nickname":"33","age":"20","lat":40.039144},{"area":"北京 东城区","picWidth":510,"createtime":1499485756927,"picHeight":507,"gender":"男","lng":0,"introduce":"嗯","imagePath":"http://dyt-pict.oss-cn-beijing.aliyuncs.com/dliao/default_man.jpg","userId":30,"yxpassword":"V69r8w6H","password":"","lasttime":1499485756927,"phone":"15012345678","nickname":"嗯嗯","age":"25","lat":0},{"area":"安徽省 安庆市 怀宁县","picWidth":510,"createtime":1499485954657,"picHeight":507,"gender":"男","lng":116.293678,"introduce":"同学","imagePath":"http://dyt-pict.oss-cn-beijing.aliyuncs.com/dliao/default_man.jpg","userId":31,"yxpassword":"5T4YjXOT","password":"","lasttime":1499485954657,"phone":"18863894003","nickname":"看看","age":"22","lat":40.039186},{"area":"请选择","picWidth":510,"createtime":1499486016104,"picHeight":507,"gender":"男","lng":0,"introduce":"1234","imagePath":"http://dyt-pict.oss-cn-beijing.aliyuncs.com/dliao/default_man.jpg","userId":32,"yxpassword":"9t314X45","password":"","lasttime":1499486016104,"phone":"13578888888","nickname":"1234","age":"23","lat":0},{"area":"安徽省 安庆市 枞阳县","picWidth":720,"createtime":1499486051103,"picHeight":960,"gender":"男","lng":116.293678,"introduce":"套路出牌","imagePath":"http://qhb.2dyt.com/MyInterface/images/b3533aad-bed7-4af6-81fc-5b9074ad5227.jpg","userId":33,"yxpassword":"lj0Al682","password":"","lasttime":1499486051103,"phone":"18863894007","nickname":"看看","age":"37","lat":40.039186},{"area":"12122222","picWidth":510,"createtime":1499486098803,"picHeight":507,"gender":"男","lng":0,"introduce":"1233333","imagePath":"http://dyt-pict.oss-cn-beijing.aliyuncs.com/dliao/default_man.jpg","userId":34,"yxpassword":"LTR11O88","password":"","lasttime":1499486098803,"phone":"18511025862","nickname":"125555","age":"14","lat":0},{"area":"江苏省 常州市 天宁区","picWidth":720,"createtime":1499486237251,"picHeight":647,"gender":"女","lng":34,"introduce":"hjlo","imagePath":"http://qhb.2dyt.com/MyInterface/images/239d0fdd-4e60-43cc-9723-4d72d69afa3f.jpg","userId":35,"yxpassword":"Kadh232W","password":"","lasttime":1499486237251,"phone":"13592001929","nickname":"hjz","age":"27","lat":40.039229},{"area":"北京 崇文区","picWidth":510,"createtime":1499486505950,"picHeight":507,"gender":"男","lng":0,"introduce":"en","imagePath":"http://dyt-pict.oss-cn-beijing.aliyuncs.com/dliao/default_man.jpg","userId":36,"yxpassword":"Sdb8W4Ov","password":"","lasttime":1499486505950,"phone":"15011111111","nickname":"都","age":"24","lat":0},{"area":"安徽省-安庆市-枞阳县","picWidth":510,"createtime":1499486589914,"picHeight":507,"gender":"男","lng":0,"introduce":"fghhh","imagePath":"http://dyt-pict.oss-cn-beijing.aliyuncs.com/dliao/default_man.jpg","userId":37,"yxpassword":"4E1oLc2L","password":"","lasttime":1499486589914,"phone":"15012345675","nickname":"qwekkk","age":"20","lat":0},{"area":"湖南 长沙 岳麓区","picWidth":510,"createtime":1499486656310,"picHeight":507,"gender":"男","lng":0,"introduce":"qwe","imagePath":"http://dyt-pict.oss-cn-beijing.aliyuncs.com/dliao/default_man.jpg","userId":38,"yxpassword":"MtsC3bKK","password":"","lasttime":1499486656310,"phone":"13565897452","nickname":"qwr","age":"24","lat":0},{"area":"安徽省-安庆市-枞阳县","picWidth":510,"createtime":1499486957473,"picHeight":507,"gender":"男","lng":0,"introduce":"gfsgsfg","imagePath":"http://dyt-pict.oss-cn-beijing.aliyuncs.com/dliao/default_man.jpg","userId":39,"yxpassword":"6Q0ra2aq","password":"","lasttime":1499486957473,"phone":"13699181606","nickname":"123","age":"20","lat":0},{"area":"安徽省 安庆市 宿松县","picWidth":510,"createtime":1499487068218,"picHeight":507,"gender":"男","lng":116.290688,"introduce":"看看","imagePath":"http://dyt-pict.oss-cn-beijing.aliyuncs.com/dliao/default_man.jpg","userId":40,"yxpassword":"819j3W87","password":"","lasttime":1499487068218,"phone":"18863894008","nickname":"看看","age":"37","lat":40.037895},{"area":"安徽省 安庆市 枞阳县","picWidth":720,"createtime":1499487649904,"picHeight":1277,"gender":"男","lng":116.290688,"introduce":"QQ","imagePath":"http://qhb.2dyt.com/MyInterface/images/1f4a47c7-1cdb-4b94-beb0-7b559b9f0d0f.jpg","userId":41,"yxpassword":"1v555z04","password":"","lasttime":1499487649904,"phone":"18863894005","nickname":"吧","age":"38","lat":40.037895},{"area":"安徽省 安庆市 枞阳县","picWidth":720,"createtime":1499488155825,"picHeight":1277,"gender":"男","lng":116.290688,"introduce":"看看","imagePath":"http://qhb.2dyt.com/MyInterface/images/bf7c2d7c-c9df-4a8e-aaba-641c609b6b13.jpg","userId":42,"yxpassword":"m8WV585Z","password":"","lasttime":1499488155825,"phone":"18863894000","nickname":"看看","age":"38","lat":40.037895},{"area":"重庆市-重庆市-巴南区","picWidth":510,"createtime":1499488231631,"picHeight":507,"gender":"男","lng":0,"introduce":"女聚聚","imagePath":"http://dyt-pict.oss-cn-beijing.aliyuncs.com/dliao/default_man.jpg","userId":43,"yxpassword":"9G44T125","password":"","lasttime":1499488231631,"phone":"13527587359","nickname":"木木木女女哦lol","age":"25","lat":0},{"area":"广东省-潮州市-潮安县","picWidth":510,"createtime":1499488776496,"picHeight":507,"gender":"男","lng":0,"introduce":"女fuel","imagePath":"http://dyt-pict.oss-cn-beijing.aliyuncs.com/dliao/default_man.jpg","userId":44,"yxpassword":"la3608r0","password":"","lasttime":1499488776496,"phone":"13568353534","nickname":"fuel父母","age":"25","lat":0}]
     * result_message : 查询成功
     * result_code : 200
     */
    private String pageCount;
    private String result_message;
    private int result_code;
    private List<DataBean> data;

    public IndexBean(String pageCount, String result_message, int result_code) {
        this.pageCount = pageCount;
        this.result_message = result_message;
        this.result_code = result_code;
    }

    public IndexBean() {
    }

    public String getPageCount() {
        return pageCount;
    }

    public void setPageCount(String pageCount) {
        this.pageCount = pageCount;
    }

    public String getResult_message() {
        return result_message;
    }

    public void setResult_message(String result_message) {
        this.result_message = result_message;
    }

    public int getResult_code() {
        return result_code;
    }

    public void setResult_code(int result_code) {
        this.result_code = result_code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }



}
