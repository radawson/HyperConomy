package org.clockworx.hyperconomy.event;

import org.clockworx.hyperconomy.transaction.PlayerTransaction;

import org.clockworx.hyperconomy.transaction.TransactionResponse;

public class TransactionEvent extends HyperEvent {
	private PlayerTransaction transaction;
	private TransactionResponse response;
	
	public TransactionEvent(PlayerTransaction transaction, TransactionResponse response) {
		this.transaction = transaction;
		this.response = response;
	}

	public PlayerTransaction getTransaction() {
		return transaction;
	}
	
	public TransactionResponse getTransactionResponse() {
		return response;
	}
}
