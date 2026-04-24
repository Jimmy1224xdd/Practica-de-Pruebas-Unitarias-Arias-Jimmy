package ec.epn.edu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SmartWalletTest {

    private SmartWallet standardWallet;
    private SmartWallet premiumWallet;

    @BeforeEach
    public void setUp() {
        standardWallet = new SmartWallet("Standard");
        premiumWallet = new SmartWallet("Premium");
    }

    // 1. Camino feliz: Depósito válido sin cashback
    @Test
    public void testDepositValidAmount() {
        boolean result = standardWallet.deposit(50.0);
        assertTrue(result);
        assertEquals(50.0, standardWallet.getBalance(), 0.001);
        assertTrue(standardWallet.isActive());
    }

    // 2. Camino feliz: Depósito con cashback
    @Test
    public void testDepositWithCashback() {
        boolean result = standardWallet.deposit(200.0);
        assertTrue(result);
        // Deposit: 200, Cashback: 200 * 0.01 = 2 -> Total: 202
        assertEquals(202.0, standardWallet.getBalance(), 0.001);
    }

    // 3. Caso de error: Monto negativo en depósito
    @Test
    public void testDepositNegativeAmount() {
        boolean result = standardWallet.deposit(-50.0);
        assertFalse(result);
        assertEquals(0.0, standardWallet.getBalance(), 0.001);
    }

    // 4. Límite: Exactamente 100 de depósito (sin cashback)
    @Test
    public void testDepositExactly100() {
        boolean result = standardWallet.deposit(100.0);
        assertTrue(result);
        assertEquals(100.0, standardWallet.getBalance(), 0.001);
    }

    // 5. Límite: Exactamente 5000 de saldo para usuario Standard
    @Test
    public void testDepositExactly5000() {
        for (int i = 0; i < 49; i++) {
            standardWallet.deposit(100.0);
        }
        boolean result = standardWallet.deposit(100.0);
        assertTrue(result, "Deposit should be allowed to exactly reach 5000. Current balance before deposit: " + standardWallet.getBalance());
        assertEquals(5000.0, standardWallet.getBalance(), 0.001);
    }

    // 6. Caso de error: Exceder límite de 5000 para Standard
    @Test
    public void testDepositExceedsLimitForStandard() {
        boolean result = standardWallet.deposit(5000.0); 
        // 5000 > 100, so cashback = 50 -> Total = 5050. This exceeds 5000.
        assertFalse(result);
        assertEquals(0.0, standardWallet.getBalance(), 0.001);
    }

    // 7. Camino feliz: Retiro válido
    @Test
    public void testWithdrawValidAmount() {
        standardWallet.deposit(100.0); // Balance: 100
        boolean result = standardWallet.withdraw(40.0);
        assertTrue(result);
        assertEquals(60.0, standardWallet.getBalance(), 0.001);
        assertTrue(standardWallet.isActive());
    }

    // 8. Caso de error: Retiro negativo
    @Test
    public void testWithdrawNegativeAmount() {
        standardWallet.deposit(100.0);
        boolean result = standardWallet.withdraw(-20.0);
        assertFalse(result);
        assertEquals(100.0, standardWallet.getBalance(), 0.001);
    }

    // 9. Caso de error: Retirar más de lo que hay en el saldo
    @Test
    public void testWithdrawInsufficientBalance() {
        standardWallet.deposit(100.0);
        boolean result = standardWallet.withdraw(150.0);
        assertFalse(result);
        assertEquals(100.0, standardWallet.getBalance(), 0.001);
    }

    // 10. Límite/Camino feliz: Retirar todo el saldo (Inactiva)
    @Test
    public void testWithdrawAllBalance() {
        standardWallet.deposit(100.0);
        boolean result = standardWallet.withdraw(100.0);
        assertTrue(result);
        assertEquals(0.0, standardWallet.getBalance(), 0.001);
        assertFalse(standardWallet.isActive());
    }
}
