import java.util.*;

class VideoData {
    String videoId;
    String data;

    VideoData(String id,String data){
        this.videoId=id;
        this.data=data;
    }
}

public class MultiLevelCache {

    int L1_SIZE = 10000;
    int L2_SIZE = 100000;

    LinkedHashMap<String,VideoData> L1 =
            new LinkedHashMap<>(L1_SIZE,0.75f,true);

    HashMap<String,String> L2 = new HashMap<>();

    HashMap<String,Integer> accessCount = new HashMap<>();

    public VideoData getVideo(String id){

        if(L1.containsKey(id)){

            System.out.println("L1 HIT");

            return L1.get(id);
        }

        if(L2.containsKey(id)){

            System.out.println("L2 HIT");

            VideoData video =
                    new VideoData(id,"Loaded from SSD");

            promoteToL1(id,video);

            return video;
        }

        System.out.println("L3 DB HIT");

        VideoData video =
                new VideoData(id,"Loaded from Database");

        addToL2(id);

        return video;
    }

    void promoteToL1(String id,VideoData video){

        if(L1.size() >= L1_SIZE){

            String firstKey =
                    L1.keySet().iterator().next();

            L1.remove(firstKey);
        }

        L1.put(id,video);
    }

    void addToL2(String id){

        if(L2.size() >= L2_SIZE){

            Iterator<String> it =
                    L2.keySet().iterator();

            if(it.hasNext())
                L2.remove(it.next());
        }

        L2.put(id,"ssd_location");
    }

    public static void main(String[] args){

        MultiLevelCache cache =
                new MultiLevelCache();

        cache.getVideo("video_123");
        cache.getVideo("video_123");
        cache.getVideo("video_999");
    }
}