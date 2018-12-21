package model;

import javax.jms.JMSConsumer;
import static model.BoardModel.MY_COLOR;
import static view.Color.*;

import jms.Consumer;
import jms.Producer;

public class MessagingModel {

	public static Producer producer;
	public static Consumer consumer;
	BoardModel model;

	static {
		try {
			if (MY_COLOR == WHITE) {
				producer = new Producer("localhost:4848/jms", "ATJ2queue");
				consumer = new Consumer("localhost:4848/jms", "ATJQueue");
			} else {
				producer = new Producer("localhost:4848/jms", "ATJQueue");
				consumer = new Consumer("localhost:4848/jms", "ATJ2queue");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sendQueueMessage(String message) {
		producer.sendQueueMessage(message);
	}

	public MessagingModel(BoardModel model) {
		this.model = model;
	}

	public String makeMsgString(Step move) {
		StringBuilder builder = new StringBuilder();
		builder.append(move.id());
		builder.append(" ");
		builder.append(model.myBallsToMove.size());
		builder.append(" ");
		for (Ball tmpBall : model.myBallsToMove) {
			builder.append(tmpBall.getX() + " " + tmpBall.getY() + " ");
		}

		builder.append(model.opponentBallsToMove.size());
		builder.append(" ");
		for (Ball tmpBall : model.opponentBallsToMove) {
			builder.append(tmpBall.getX() + " " + tmpBall.getY() + " ");
		}

		return builder.toString();
	}

	public Step readFromMsgString(String message) {
		String[] data = message.split(" ");
		Step move = Step.getFromID(Integer.parseInt(data[0]));
		int opponentBallsLength = Integer.parseInt(data[1]);
		for (int i = 2; i < 2 * opponentBallsLength + 2; i += 2) {
			int x = Integer.parseInt(data[i]);
			int y = Integer.parseInt(data[i + 1]);
			Ball tmpBall = model.opponentBalls.getBall(x, y);
			System.out.println(tmpBall + "x" + x + "y" + y);
			if (tmpBall != null)
				model.myBallsToMove.add(tmpBall);
		}
		int myBallsLength = Integer.parseInt(data[2 * opponentBallsLength + 2]);
		for (int i = 2 * opponentBallsLength + 3; i < 2 * myBallsLength + 2 * opponentBallsLength + 3; i += 2) {
			int x = Integer.parseInt(data[i]);
			int y = Integer.parseInt(data[i + 1]);
			Ball tmpBall = model.myBalls.getBall(x, y);
			System.out.println("x2" + x + "y2" + y);
			if (tmpBall != null)
				model.opponentBallsToMove.add(tmpBall);
		}
		return move;

	}

	public static JMSConsumer getJmsConsumer() {
		return consumer.getJmsConsumer();
	}

}
