package it.polito.dp2.rest.rns.utility;

public class Counter {
	private String name;
	private long counter;
	private long reservations;
	
	public Counter() {
		this.name = "counter";
		this.counter = 0;
		this.reservations = 0;
	}

	public Counter(String name, long counter) {
		this.name = name;
		this.counter = counter;
		this.reservations = 0;
	}

	public Counter(String name, long counter, long reservations) {
		this.name = name;
		this.counter = counter;
		this.reservations = reservations;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getCounter() {
		return counter;
	}

	public void setCounter(long counter) {
		this.counter = counter;
	}
	
	public void incrementCounter(long amount) {
		this.counter += amount;
	}
	
	public void decrementCounter(long amount) {
		this.counter -= amount;
	}

	public long getReservations() {
		return reservations;
	}

	public void setReservations(long reservations) {
		this.reservations = reservations;
	}
	
	public void addReservation() {
		this.reservations++;
	}
	
	public void removeReservation() {
		this.reservations--;
	}
	
}
