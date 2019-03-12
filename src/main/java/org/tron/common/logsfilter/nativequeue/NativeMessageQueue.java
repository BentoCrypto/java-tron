package org.tron.common.logsfilter.nativequeue;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;

import java.util.Objects;

public class NativeMessageQueue {
  ZMQ.Context context = null;
  private ZMQ.Socket publisher = null;
  private static NativeMessageQueue instance;
  private static final int DEFAULT_BIND_PORT = 5555;
  private int bindPort = 0;
  public static NativeMessageQueue getInstance() {
      if (Objects.isNull(instance)) {
          synchronized (NativeMessageQueue.class) {
              if (Objects.isNull(instance)) {
                  instance = new NativeMessageQueue();
              }
          }
      }
      return instance;
  }

  public void start(int bindPort){
      context = ZMQ.context(1);
      publisher = context.socket(SocketType.PUB);

      if (bindPort == 0){
        bindPort = DEFAULT_BIND_PORT;
      }

      String bindAddress = String.format("tcp://*:%d", bindPort);
      publisher.bind(bindAddress);
  }

  public void stop(){
      if (Objects.nonNull(publisher)){
          publisher.close();
      }

      if (Objects.nonNull(context)){
          context.term();
      }
  }

  public void publishTrigger(String data, String topic){
    if (Objects.isNull(publisher)) {
      return;
    }

    publisher.sendMore(topic);
    publisher.send(data);

    System.out.println("topic " + topic);
    System.out.println("trigger " + data);
  }
}
