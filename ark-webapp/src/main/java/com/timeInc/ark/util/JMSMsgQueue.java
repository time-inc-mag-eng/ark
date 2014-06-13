/*******************************************************************************
 * Copyright 2014 Time Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.timeInc.ark.util;

import java.util.Map;
import java.util.UUID;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.log4j.Logger;

import com.timeInc.mageng.util.exceptions.PrettyException;

/**
 * Enqueues a message in a JMS Queue
 */
public class JMSMsgQueue {
	private static final Logger log = Logger.getLogger(JMSMsgQueue.class);
	
	private final ActiveMQConnectionFactory connectionFactory;
	private final String queueName;
	
	/**
	 * Instantiates a new JMS msg sender.
	 *
	 * @param connectionFactory the connection factory
	 * @param queueName the queue name
	 */
	public JMSMsgQueue(ActiveMQConnectionFactory connectionFactory, String queueName) {
		this.connectionFactory = connectionFactory;
		this.queueName = queueName;
	}
	
	/**
	 * Instantiates a new JMS msg sender.
	 *
	 * @param brokerUrl the broker url
	 * @param queueName the queue name
	 */
	public JMSMsgQueue(String brokerUrl, String queueName) {
		this(new ActiveMQConnectionFactory(brokerUrl), queueName);
	}
	
	/**
	 * Enqueues a message using the specified property and message map.
	 * A wait time of 10 seconds is used to wait for any response
	 *
	 * @param properties the properties
	 * @param messages the messages
	 * @return the string the response, null if wait time exceeded
	 */
	public String enqueue(Map<String, Object> properties, Map<String, Object> messages) {
		Connection connection = null;
		Session session = null;
		MessageProducer producer = null;
		MessageConsumer consumer = null;

		try {
            connection = connectionFactory.createConnection(); // Create the connection.
            connection.start();

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(queueName);
            producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            
            Destination tempDest = session.createTemporaryQueue();
            consumer = session.createConsumer(tempDest);
            
            MapMessage mapMsg = new ActiveMQMapMessage();
            mapMsg.setJMSReplyTo(tempDest);
            mapMsg.setJMSCorrelationID(UUID.randomUUID().toString());
            
            for(Map.Entry<String,Object> keyPair : messages.entrySet()) {
            	addToMsgMap(mapMsg, keyPair.getKey(), keyPair.getValue());
            }
            
            for(Map.Entry<String,Object> keyPair : properties.entrySet()) {
            	addToPropertyMap(mapMsg, keyPair.getKey(), keyPair.getValue());
            }
            
            producer.send(mapMsg);
            
            // let's check for the message response
    		Message message =  consumer.receive(1000 * 10); // wait up to 10 seconds to get response
    		
    		if(message instanceof TextMessage) {
            	TextMessage txtMsg = (TextMessage) message;
            	return txtMsg.getText();
            } else
            	return null;
    		
        } catch (JMSException jmsEx) {
        	throw new PrettyException("An error occurred while trying to send a msg", jmsEx);
        } finally {
        	closeQuietly(producer);
        	closeQuietly(consumer);
        	closeQuietly(session);
        	closeQuietly(connection);
        }		
	}
	
	
	private static void addToMsgMap(MapMessage mapMsg, String key, Object value) throws JMSException {
		if(Integer.class.isInstance(value)) {
			mapMsg.setInt(key, (Integer) value);
		} else if(Long.class.isInstance(value)) {
			mapMsg.setLong(key, (Long) value);
		} else if(Boolean.class.isInstance(value)) {
			mapMsg.setBoolean(key, (Boolean) value);
		} else if(String.class.isInstance(value)) { 
			mapMsg.setString(key, (String) value);
		} else 
			mapMsg.setObject(key, value);
	}
	
	private static void addToPropertyMap(MapMessage mapMsg, String key, Object value) throws JMSException {
		if(Integer.class.isInstance(value)) {
			mapMsg.setIntProperty(key, (Integer) value);
		} else if(Long.class.isInstance(value)) {
			mapMsg.setLongProperty(key, (Long) value);
		} else if(Boolean.class.isInstance(value)) {
			mapMsg.setBooleanProperty(key, (Boolean) value);
		} else if(String.class.isInstance(value)) { 
			mapMsg.setStringProperty(key, (String) value);
		} else 
			mapMsg.setObjectProperty(key, value);
	}
	
	
	private static void closeQuietly(MessageProducer producer) {
		try {
			if(producer != null)
				producer.close();
		} catch(JMSException ex) {
			log.warn("Failed to close producer",ex);
		}
	}
	
	private static void closeQuietly(MessageConsumer consumer) {
		try {
			if(consumer != null)
				consumer.close();
		} catch(JMSException ex) {
			log.warn("Failed to close consumer",ex);
		}
	}
	
	private static void closeQuietly(Session session) {
		try {
			if(session != null)
				session.close();
		} catch(JMSException ex) {
			log.warn("Failed to close session",ex);
		}
	}
	
	private static void closeQuietly(Connection connection) {
		try {
			if(connection != null)
				connection.close();
		} catch(JMSException ex) {
			log.warn("Failed to close connection",ex);
		}
	}
}
