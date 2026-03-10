import java.util.*;

class Transaction {

    int id;
    int amount;
    String merchant;
    String account;
    long time;

    Transaction(int id,int amount,String merchant,String account,long time){
        this.id=id;
        this.amount=amount;
        this.merchant=merchant;
        this.account=account;
        this.time=time;
    }
}

public class FraudDetector {

    public static void findTwoSum(List<Transaction> list,int target){

        HashMap<Integer,Transaction> map=new HashMap<>();

        for(Transaction t:list){

            int complement=target-t.amount;

            if(map.containsKey(complement)){

                Transaction other=map.get(complement);

                System.out.println("Pair Found: "+
                        other.id+" & "+t.id);
            }

            map.put(t.amount,t);
        }
    }

    public static void detectDuplicates(List<Transaction> list){

        HashMap<String,List<Transaction>> map=new HashMap<>();

        for(Transaction t:list){

            String key=t.amount+"-"+t.merchant;

            map.putIfAbsent(key,new ArrayList<>());

            map.get(key).add(t);
        }

        for(String key:map.keySet()){

            List<Transaction> group=map.get(key);

            if(group.size()>1){

                System.out.println("Duplicate detected for "+key);
            }
        }
    }

    public static void main(String[] args){

        List<Transaction> list=new ArrayList<>();

        list.add(new Transaction(1,500,"StoreA","acc1",0));
        list.add(new Transaction(2,300,"StoreB","acc2",0));
        list.add(new Transaction(3,200,"StoreC","acc3",0));

        findTwoSum(list,500);

        detectDuplicates(list);
    }
}