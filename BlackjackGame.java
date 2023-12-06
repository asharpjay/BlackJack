import java.util.Scanner;

public class BlackjackGame {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Blackjack!");

        // Get player's name
        System.out.print("Enter your name: ");
        String playerName = scanner.nextLine();

        // Create the player and dealer
        Player player = new Player(playerName);
        Player dealer = new Player("Dealer");

        // Create and shuffle the deck
        Deck deck = new Deck();
        deck.shuffle();

        // Deal initial cards
        player.addCard(deck.dealCard());
        dealer.addCard(deck.dealCard());
        player.addCard(deck.dealCard());
        dealer.addCard(deck.dealCard());

        // Display initial hands
        System.out.println("\nInitial hands:");
        player.displayHand();
        dealer.displayPartialHand(); // Show only the first card of the dealer

        // Player's turn
        while (player.getScore() < 21) {
            System.out.print("\nDo you want to hit or stand? (h/s): ");
            char choice = scanner.next().charAt(0);

            if (choice == 'h') {
                player.addCard(deck.dealCard());
                player.displayHand();
            } else if (choice == 's') {
                break;
            }
        }

        // Dealer's turn
        System.out.println("\nDealer's turn:");
        dealer.displayHand();

        while (dealer.getScore() < 17) {
            dealer.addCard(deck.dealCard());
            dealer.displayHand();
        }

        // Display final hands
        System.out.println("\nFinal hands:");
        player.displayHand();
        dealer.displayHand();

        // Determine the winner
        determineWinner(player, dealer);

        // Close the scanner
        scanner.close();
    }

    private static void determineWinner(Player player, Player dealer) {
        int playerScore = player.getScore();
        int dealerScore = dealer.getScore();

        System.out.println("\nGame Over!");

        if (playerScore > 21) {
            System.out.println(player.getName() + " busts! Dealer wins.");
        } else if (dealerScore > 21) {
            System.out.println("Dealer busts! " + player.getName() + " wins.");
        } else if (playerScore > dealerScore) {
            System.out.println(player.getName() + " wins!");
        } else if (playerScore < dealerScore) {
            System.out.println("Dealer wins.");
        } else {
            System.out.println("It's a tie!");
        }
    }
}

class Player {
    private String name;
    private int score;
    private Hand hand;

    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.hand = new Hand();
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void addCard(Card card) {
        hand.addCard(card);
        score = hand.calculateScore();
    }

    public void displayHand() {
        System.out.println(name + "'s hand: " + hand);
        System.out.println("Score: " + score);
    }

    public void displayPartialHand() {
        System.out.println(name + "'s hand: " + hand.getPartialHand());
    }
}

class Hand {
    private java.util.List<Card> cards;

    public Hand() {
        this.cards = new java.util.ArrayList<>();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public int calculateScore() {
        int score = 0;
        int numAces = 0;

        for (Card card : cards) {
            score += card.getValue();

            if (card.getRank().equals("Ace")) {
                numAces++;
            }
        }

        // Handle Aces as 1 or 11 based on the situation
        while (numAces > 0 && score > 21) {
            score -= 10;
            numAces--;
        }

        return score;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();

        for (Card card : cards) {
            result.append(card).append(" ");
        }

        return result.toString().trim();
    }

    public String getPartialHand() {
        return cards.get(0).toString() + " [Hidden]";
    }
}

class Deck {
    private java.util.List<Card> cards;
    private java.util.Random random;

    public Deck() {
        this.cards = new java.util.ArrayList<>();
        this.random = new java.util.Random();

        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};

        for (String rank : ranks) {
            for (String suit : suits) {
                cards.add(new Card(rank, suit));
            }
        }
    }

    public void shuffle() {
        java.util.Collections.shuffle(cards, random);
    }

    public Card dealCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("Deck is empty. Cannot deal a card.");
        }
        return cards.remove(0);
    }
}

class Card {
    private String rank;
    private String suit;

    public Card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public String getRank() {
        return rank;
    }

    public String getSuit() {
        return suit;
    }

    public int getValue() {
        switch (rank) {
            case "Jack":
            case "Queen":
            case "King":
                return 10;
            case "Ace":
                return 11;
            default:
                return Integer.parseInt(rank);
        }
    }

    public String toString() {
        return rank + " of " + suit;
    }
}