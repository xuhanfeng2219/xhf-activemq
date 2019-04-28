/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.activemq;

import java.util.List;
import javax.jms.JMSException;
import org.apache.activemq.command.MessageDispatch;

public interface MessageDispatchChannel {

    public abstract void enqueue(MessageDispatch message);

    public abstract void enqueueFirst(MessageDispatch message);

    public abstract boolean isEmpty();

    /**
     * Used to get an enqueued message. The amount of time this method blocks is
     * based on the timeout value. - if timeout==-1 then it blocks until a
     * message is received. - if timeout==0 then it it tries to not block at
     * all, it returns a message if it is available - if timeout>0 then it
     * blocks up to timeout amount of time. Expired messages will consumed by
     * this method.
     *
     * 用于获取排队的消息。
     * 此方法阻止的时间量基于超时值。
     * -如果超时==-1则阻塞直到收到消息。
     * -如果超时==0那么它会尝试不阻塞，如果可用则返回一条消息。
     * -如果超时>0则它会阻塞超时时间。
     * 此方法将使用过期的消息。
     * 
     * @throws JMSException
     * @return null if we timeout or if the consumer is closed.
     * @throws InterruptedException
     */
    public abstract MessageDispatch dequeue(long timeout) throws InterruptedException;

    public abstract MessageDispatch dequeueNoWait();

    public abstract MessageDispatch peek();

    public abstract void start();

    public abstract void stop();

    public abstract void close();

    public abstract void clear();

    public abstract boolean isClosed();

    public abstract int size();

    public abstract Object getMutex();

    public abstract boolean isRunning();

    public abstract List<MessageDispatch> removeAll();

}