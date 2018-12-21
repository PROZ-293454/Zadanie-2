package jms;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.TextMessage;

public class Consumer {
	private JMSContext jmsContext;
	private JMSConsumer jmsConsumer;
	private Queue queue;

	public Consumer(String url, String queueName) throws JMSException {
		ConnectionFactory connectionFactory = new com.sun.messaging.ConnectionFactory();
		jmsContext = connectionFactory.createContext();
		((com.sun.messaging.ConnectionFactory) connectionFactory)
				.setProperty(com.sun.messaging.ConnectionConfiguration.imqAddressList, url);
		queue = new com.sun.messaging.Queue(queueName); // "ATJQueue"
		jmsConsumer = jmsContext.createConsumer(queue);
	}

	public String receiveQueueMessage() throws JMSException, InterruptedException {
		Message msg = jmsConsumer.receive(10); // 10 ms
		if (msg instanceof TextMessage)
			return ((TextMessage) msg).getText();
		return null;
	}

	protected void finalize() {
		if (jmsConsumer != null)
			jmsConsumer.close();
		if (jmsContext != null)
			jmsContext.close();
	}

	public JMSConsumer getJmsConsumer() {
		return jmsConsumer;
	}

	public void receiveQueueMessageAsync() throws InterruptedException {
		jmsConsumer.setMessageListener(new AsynchConsumer());
		for (int i = 0; i < 3000; ++i) { // 30 sekund
			System.out.println(i);
			Thread.sleep(1);
		}
	}
}