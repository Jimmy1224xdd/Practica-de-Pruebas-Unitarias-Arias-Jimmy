package ec.epn.edu;

// Clase que representa una billetera inteligente
public class SmartWallet {
    // Variable para guardar el dinero que tenemos en la billetera
    private double balance;
    // Tipo de usuario: puede ser "Standard" u otros tipos
    private String userType;
    // Indica si la billetera está activa (funcionando) o no
    private boolean isActive;

    // Constructor: cuando creamos una nueva billetera, le decimos qué tipo de
    // usuario es
    public SmartWallet(String userType) {
        this.userType = userType;
        this.balance = 0.0; // Al inicio no hay dinero
        this.isActive = true; // La billetera empieza activa
    }

    public boolean deposit(double amount) {
        // No se permite depositar cantidades negativas o cero
        if (amount <= 0) {
            return false;
        }

        // Si depositamos más de 100, obtenemos un 1% de cashback
        double cashback = 0;
        if (amount > 100) {
            cashback = amount * 0.01;
        }

        // Calculamos el nuevo saldo
        double newBalance = balance + amount + cashback;

        // Los usuarios "Standard" no pueden pasar de 5000 de saldo
        if ("Standard".equals(userType) && newBalance > 5000) {
            return false;
        }

        // Actualizamos el saldo
        balance = newBalance;

        // Si el saldo es positivo, la billetera sigue activa
        if (balance > 0) {
            isActive = true;
        }
        return true;
    }

    public boolean withdraw(double amount) {
        if (amount <= 0) {
            return false;
        }

        if (amount > balance) {
            return false;
        }

        balance -= amount;

        if (balance == 0) {
            isActive = false;
        }
        return true;
    }

    public double getBalance() {
        return balance;
    }

    public String getUserType() {
        return userType;
    }

    public boolean isActive() {
        return isActive;
    }
}
