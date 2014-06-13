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

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

/**
 * Sends a message to an ActiveMQ topic.
 */
public class JMSTopicSender {
	private static final Logger log = Logger.getLogger(JMSTopicSender.class);
	
	private final ActiveMQConnectionFactory connectionFactory; // thread-safe
	private final String topic;
	
	/**
	 * Instantiates a new JMS topic sender.
	 *
	 * @param connectionFactory the connection factory
	 * @param topic the topic
	 */
	public JMSTopicSender(ActiveMQConnectionFactory connectionFactory, String topic) {
		this.connectionFactory = connectionFactory;
		this.topic = topic;
	}
	
	/**
	 * Instantiates a new JMS topic sender.
	 *
	 * @param brokerUrl the broker url
	 * @param topic the topic
	 */
	public JMSTopicSender(String brokerUrl, String topic) {
		this(new ActiveMQConnectionFactory(brokerUrl),topic);
	}
	
	
	/**
	 * Send message to the topic 
	 * using the provided map.
	 *
	 * @param messageMap the message map
	 */
	public void sendMessage(Map<String,String> messageMap) {
		log.debug("Sending topic:" + topic + " with message:" + messageMap.toString());
		
		Connection connection = null;
		Session session = null;
		MessageProducer producer = null;

		try {
            connection = connectionFactory.createConnection(); // Create the connection.
            connection.start();

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            
            Topic aTopic = session.createTopic(topic);

            producer = session.createProducer(aTopic);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            
            Message message = session.createMessage();
            
            for(Map.Entry<String,String> keyPair : messageMap.entrySet()) {
            	message.setStringProperty(keyPair.getKey(),keyPair.getValue());
            }
            
            producer.send(message);
		} catch(Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			closeQuietly(producer);
			closeQuietly(session);
			closeQuietly(connection);
		}
	}
	
	private static void closeQuietly(MessageProducer producer) {
		try {
			if(producer != null)
				producer.close();
		} catch(JMSException ex) {
			log.warn("Failed to close producer",ex);
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
