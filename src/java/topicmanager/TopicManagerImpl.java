package topicmanager;

import util.Subscription_check;
import util.Topic;
import util.Topic_check;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import publisher.Publisher;
import publisher.PublisherImpl;
import subscriber.Subscriber;
import util.Subscription_close;

public class TopicManagerImpl implements TopicManager {

  private Map<Topic, Publisher> topicMap;

  public TopicManagerImpl() {
    topicMap = new HashMap<Topic, Publisher>();
  }

    @Override
    public Publisher addPublisherToTopic(Topic topic) {
        Publisher p = new PublisherImpl(topic);
        int i = 0;
        if(topicMap.containsKey(topic)){
            p = topicMap.get(topic);
            i = p.incPublishers();
        }else{
            topicMap.put(topic, p);
            i=1;
        }
        System.out.println("Add Publisher to: "+ topic.name +" number: "+i);
        return p;
    }

    @Override
    public void removePublisherFromTopic(Topic topic) {
        int i=0;
        if(topicMap.containsKey(topic)){
            Publisher p = topicMap.get(topic);
            i = p.decPublishers();
            if(i==0){
                System.out.println("Remove topic from hasmap");
                topicMap.remove(topic);
            }
        }
        System.out.println("Remove Publisher to: "+ topic.name +" number: "+i);
    }

    @Override
    public Topic_check isTopic(Topic topic) {
        Topic_check t = new Topic_check(topic, true);
        if (topicMap.containsKey(topic)) {
            t.isOpen = true;
        } else {
            t.isOpen = false;
        }
        return t;
    }

    @Override
    public List<Topic> topics() {
        ArrayList<Topic> arrayListTopic = new ArrayList<Topic>();
        Iterator hmIterator = topicMap.entrySet().iterator();
        while (hmIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry) hmIterator.next();
            arrayListTopic.add((Topic) mapElement.getKey());
        }
        System.out.println("Size :" + arrayListTopic.size());
        return arrayListTopic;
    }

    @Override
    public Subscription_check subscribe(Topic topic, Subscriber subscriber) {
        Subscription_check subscription_check = new Subscription_check(topic, Subscription_check.Result.NO_TOPIC);
        if(isTopic(topic).isOpen){
            PublisherImpl p =(PublisherImpl) topicMap.get(topic);
            p.attachSubscriber(subscriber);
            subscription_check.result = Subscription_check.Result.OKAY;
        }
        return subscription_check;
    }

    @Override
    public Subscription_check unsubscribe(Topic topic, Subscriber subscriber) {
        Subscription_check subscription_check = new Subscription_check(topic, Subscription_check.Result.NO_TOPIC);
        if(isTopic(topic).isOpen){
            PublisherImpl p =(PublisherImpl) topicMap.get(topic);
            subscriber.onClose(new Subscription_close(topic, Subscription_close.Cause.SUBSCRIBER));
            p.detachSubscriber(subscriber);
            subscription_check.result = Subscription_check.Result.NO_SUBSCRIPTION;
        }
        return subscription_check;
    }
  
  public Publisher publisher(Topic topic){
    return topicMap.get(topic);
  }
  
}
