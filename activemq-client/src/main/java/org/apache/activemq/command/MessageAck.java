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
package org.apache.activemq.command;

import org.apache.activemq.state.CommandVisitor;

/**
 * @openwire:marshaller code="22"
 * 
 */
public class MessageAck extends BaseCommand {

    public static final byte DATA_STRUCTURE_TYPE = CommandTypes.MESSAGE_ACK;

    /**
     * Used to let the broker know that the message has been delivered to the
     * client. Message will still be retained until an standard ack is received.
     * This is used get the broker to send more messages past prefetch limits
     * when an standard ack has not been sent.
     *
     * 用于让经纪人知道消息已经传递给了客户。在收到标准确认之前，消息仍将保留。
     * 这用于让代理在尚未发送标准确认时发送超过预取限制的更多消息。
     */
    public static final byte DELIVERED_ACK_TYPE = 0;

    /**
     * The standard ack case where a client wants the message to be discarded.
     * 客户希望丢弃消息的标准ack案例
     */
    public static final byte STANDARD_ACK_TYPE = 2;

    /**
     * In case the client want's to explicitly let the broker know that a
     * message was not processed and the message was considered a poison
     * message.
     *
     * 如果客户端希望明确地让代理知道消息未被处理并且消息被视为有害消息。
     */
    public static final byte POSION_ACK_TYPE = 1;

    /**
     * In case the client want's to explicitly let the broker know that a
     * message was not processed and it was re-delivered to the consumer
     * but it was not yet considered to be a poison message.  The messageCount 
     * field will hold the number of times the message was re-delivered.
     *
     * 如果客户端希望明确地让代理知道消息没有被处理并且它被重新传递给消费者但是它还没有被认为是有害消息。
     * messageCount字段将保留重新传递邮件的次数。
     */
    public static final byte REDELIVERED_ACK_TYPE = 3;
    
    /**
     * The  ack case where a client wants only an individual message to be discarded.
     * 客户端只想丢弃单个消息的ack情况。
     */
    public static final byte INDIVIDUAL_ACK_TYPE = 4;

/**
     * The ack case where a durable topic subscription does not match a selector.
 *      ack案例，其中持久主题订阅与选择器不匹配。
     */
    public static final byte UNMATCHED_ACK_TYPE = 5;

    /**
     * the case where a consumer does not dispatch because message has expired inflight
     * 消费者因为消息已经过期而没有派遣的情况
     */
    public static final byte EXPIRED_ACK_TYPE = 6;

    protected byte ackType;
    protected ConsumerId consumerId;
    protected MessageId firstMessageId;
    protected MessageId lastMessageId;
    protected ActiveMQDestination destination;
    protected TransactionId transactionId;
    protected int messageCount;
    protected Throwable poisonCause;

    protected transient String consumerKey;

    public MessageAck() {
    }

    public MessageAck(MessageDispatch md, byte ackType, int messageCount) {
        this.ackType = ackType;
        this.consumerId = md.getConsumerId();
        this.destination = md.getDestination();
        this.lastMessageId = md.getMessage().getMessageId();
        this.messageCount = messageCount;
    }

    public MessageAck(Message message, byte ackType, int messageCount) {
        this.ackType = ackType;
        this.destination = message.getDestination();
        this.lastMessageId = message.getMessageId();
        this.messageCount = messageCount;
    }

    public void copy(MessageAck copy) {
        super.copy(copy);
        copy.firstMessageId = firstMessageId;
        copy.lastMessageId = lastMessageId;
        copy.destination = destination;
        copy.transactionId = transactionId;
        copy.ackType = ackType;
        copy.consumerId = consumerId;
    }

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public boolean isMessageAck() {
        return true;
    }

    public boolean isPoisonAck() {
        return ackType == POSION_ACK_TYPE;
    }

    public boolean isStandardAck() {
        return ackType == STANDARD_ACK_TYPE;
    }

    public boolean isDeliveredAck() {
        return ackType == DELIVERED_ACK_TYPE;
    }
    
    public boolean isRedeliveredAck() {
        return ackType == REDELIVERED_ACK_TYPE;
    }
    
    public boolean isIndividualAck() {
        return ackType == INDIVIDUAL_ACK_TYPE;
    }

    public boolean isUnmatchedAck() {
        return ackType == UNMATCHED_ACK_TYPE;
    }

    public boolean isExpiredAck() {
        return ackType == EXPIRED_ACK_TYPE;
    }

    /**
     * @openwire:property version=1 cache=true
     */
    public ActiveMQDestination getDestination() {
        return destination;
    }

    public void setDestination(ActiveMQDestination destination) {
        this.destination = destination;
    }

    /**
     * @openwire:property version=1 cache=true
     */
    public TransactionId getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(TransactionId transactionId) {
        this.transactionId = transactionId;
    }

    public boolean isInTransaction() {
        return transactionId != null;
    }

    /**
     * @openwire:property version=1 cache=true
     */
    public ConsumerId getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(ConsumerId consumerId) {
        this.consumerId = consumerId;
    }

    /**
     * @openwire:property version=1
     */
    public byte getAckType() {
        return ackType;
    }

    public void setAckType(byte ackType) {
        this.ackType = ackType;
    }

    /**
     * @openwire:property version=1
     */
    public MessageId getFirstMessageId() {
        return firstMessageId;
    }

    public void setFirstMessageId(MessageId firstMessageId) {
        this.firstMessageId = firstMessageId;
    }

    /**
     * @openwire:property version=1
     */
    public MessageId getLastMessageId() {
        return lastMessageId;
    }

    public void setLastMessageId(MessageId lastMessageId) {
        this.lastMessageId = lastMessageId;
    }

    /**
     * The number of messages being acknowledged in the range.
     * 
     * @openwire:property version=1
     */
    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    /**
     * The cause of a poison ack, if a message listener
     * throws an exception it will be recorded here
     *
     * @openwire:property version=7
     */
    public Throwable getPoisonCause() {
        return poisonCause;
    }

    public void setPoisonCause(Throwable poisonCause) {
        this.poisonCause = poisonCause;
    }

    public Response visit(CommandVisitor visitor) throws Exception {
        return visitor.processMessageAck(this);
    }

    /**
     * A helper method to allow a single message ID to be acknowledged
     */
    public void setMessageID(MessageId messageID) {
        setFirstMessageId(messageID);
        setLastMessageId(messageID);
        setMessageCount(1);
    }

}
