package com.mp5a5.www.httprequest.net.entity;

import com.google.gson.annotations.SerializedName;
import com.mp5a5.www.library.net.revert.BaseResponseEntity;
import com.mp5a5.www.library.utils.ApiConfig;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author ：mp5a5 on 2018/12/28 19：42
 * @describe
 * @email：wwb199055@126.com
 */
public class NBAJEntity extends BaseResponseEntity {


    @SerializedName("error_code")
    private int code;

    @SerializedName("reason")
    private String msg;

    @Nullable
    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public boolean success() {
        return ApiConfig.getSucceedCode() == code;
    }

    @Override
    public boolean tokenInvalid() {
        return ApiConfig.getInvalidToken() == code;
    }

    @Override
    public boolean quitApp() {
        return ApiConfig.getQuitCode() == code;
    }

    public ResultBean result;


    public static class ResultBean {


        public String title;
        public StatuslistBean statuslist;
        public List<ListBean> list;
        public List<TeammatchBean> teammatch;


        public static class StatuslistBean {
            /**
             * st0 : 未开赛
             * st1 : 直播中
             * st2 : 已结束
             */

            public String st0;
            public String st1;
            public String st2;


        }

        public static class ListBean {


            public String title;
            public List<TrBean> tr;
            public List<BottomlinkBean> bottomlink;
            public List<LiveBean> live;
            public List<LivelinkBean> livelink;


            public static class TrBean {


                public String link1text;
                public String link1url;
                public String link2text;
                public String link2url;
                public String player1;
                public String player1logo;
                public String player1logobig;
                public String player1url;
                public String player2;
                public String player2logo;
                public String player2logobig;
                public String player2url;
                public String score;
                public int status;
                public String time;


            }

            public static class BottomlinkBean {

                public String text;
                public String url;

            }

            public static class LiveBean {


                public String date;
                public String liveurl;
                public String player1;
                public String player1info;
                public String player1location;
                public String player1logo;
                public String player1logobig;
                public String player1url;
                public String player2;
                public String player2info;
                public String player2location;
                public String player2logo;
                public String player2logobig;
                public String player2url;
                public String score;
                public int status;
                public String title;


            }

            public static class LivelinkBean {
                /**
                 * text : 视频直播
                 * url : http://video.sina.com.cn/l/pl/sportstv/1691558.html
                 * videoicon : 1
                 */

                public String text;
                public String url;
                public String videoicon;


            }
        }

        public static class TeammatchBean {
            /**
             * name : 老鹰
             * url : http://nba.sports.sina.com.cn/team_match.php?id=1
             */

            public String name;
            public String url;

        }
    }
}
