import java.util.logging.*;

public class Main {
    public static final String AUSTIN_POWERS = "Austin Powers";
    public static final String WEAPONS = "weapons";
    public static final String BANNED_SUBSTANCE = "banned substance";
    public static interface Sendable {
        String getFrom();
        String getTo();
    }
    public static abstract class AbstractSendable implements Sendable {

        protected final String from;
        protected final String to;

        public AbstractSendable(String from, String to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public String getFrom() {
            return from;
        }

        @Override
        public String getTo() {
            return to;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AbstractSendable that = (AbstractSendable) o;

            if (!from.equals(that.from)) return false;
            if (!to.equals(that.to)) return false;

            return true;
        }

    }
    public static class MailMessage extends AbstractSendable {

        private final String message;

        public MailMessage(String from, String to, String message) {
            super(from, to);
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            MailMessage that = (MailMessage) o;

            if (message != null ? !message.equals(that.message) : that.message != null) return false;

            return true;
        }

    }
    public static class MailPackage extends AbstractSendable {
        private final Package content;

        public MailPackage(String from, String to, Package content) {
            super(from, to);
            this.content = content;
        }

        public Package getContent() {
            return content;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            MailPackage that = (MailPackage) o;

            if (!content.equals(that.content)) return false;

            return true;
        }

    }
    public static class Package {
        private final String content;
        private final int price;

        public Package(String content, int price) {
            this.content = content;
            this.price = price;
        }

        public String getContent() {
            return content;
        }

        public int getPrice() {
            return price;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Package aPackage = (Package) o;

            if (price != aPackage.price) return false;
            if (!content.equals(aPackage.content)) return false;

            return true;
        }
    }

    public static interface MailService {
        Sendable processMail(Sendable mail);
    }

    public static class RealMailService implements MailService {

        @Override
        public Sendable processMail(Sendable mail) {
            // Здесь описан код настоящей системы отправки почты.
            return mail;
        }
    }

    //инспектор и недобросовестного
    public static class Thief implements MailService{
        int minCoast;
        int stolenValue;
        public Thief(int cost) {
            minCoast = cost;
        }
        int getStolenValue() {
            return stolenValue;
        }

        @Override
        public Sendable processMail(Sendable mail) {
            MailPackage mailPackage = null;
            if (mail instanceof MailPackage) {
                mailPackage = (MailPackage) mail;
                if (mailPackage.getContent().getPrice() >= minCoast) {
                    stolenValue+=mailPackage.getContent().getPrice();
                    mail = new MailPackage(mail.getFrom(), mail.getTo(), new Package(String.format("stones instead of %s",mailPackage.getContent().getContent()),0));
                }
            }
            return mail;
        }
    }

    public static class Spy implements MailService {
        final Logger LOGGER = Logger.getLogger(Spy.class.getName());
        @Override
        public Sendable processMail(Sendable mail) {
            if (mail instanceof MailMessage) {
                MailMessage mailMessage = (MailMessage) mail;
                if (mailMessage.getFrom() == AUSTIN_POWERS) {
                    LOGGER.log(Level.WARNING, "Detected target mail correspondence: from {0} to {1} \"{2}\"",new Object[]{mailMessage.getFrom(), mailMessage.getTo(), mailMessage.getMessage()});
                } else {
                    LOGGER.log(Level.INFO, "Usual correspondence: from {0} to {1}",new Object[]{mailMessage.getFrom(), mailMessage.getTo()});
                }
            }
            return mail;
        }
    }

    public static class Inspector implements MailService {

        @Override
        public Sendable processMail(Sendable mail) {
            return mail;
        }
    }
    public static void main(String[] args) {

    }
}