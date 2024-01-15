/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package primecntprovider;

/**
 *
 * @author TUFGAMING
 */
public class PrimeCountMachine {
    public int countPrime(int startVal, int endVal) {
        int cnt = 0;
        for (int i = startVal; i <= endVal; i++) {
            if (isPrime(i)) {
                cnt++ ;
            }
        }
        return cnt;
    }
    
    public boolean isPrime(int n) {
        int i;
        for (i = 2; i*i <= n; i++) {
            if ((n % i) == 0) {
                return false;
            }
        }
        return true;
    }
}


