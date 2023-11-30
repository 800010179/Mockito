import MockitoEsimerkki.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TilaustenKäsittelyMockitoTestPrice {
    @Mock
    IHinnoittelija hinnoittelijaMock;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPriceAbove100() {
        // Arrange
        float alkuSaldo = 150.0f;
        float listaHinta = 120.0f;
        float alennus = 10.0f;
        float loppuSaldo = alkuSaldo - (listaHinta * (1 - alennus / 100));
        Asiakas asiakas = new Asiakas(alkuSaldo);
        Tuote tuote = new Tuote("Over 100 Thing", listaHinta);

        // Record
        when(hinnoittelijaMock.getAlennusProsentti(asiakas, tuote))
                .thenReturn(alennus);

        // Act
        TilaustenKäsittely käsittelijä = new TilaustenKäsittely();
        käsittelijä.setHinnoittelija(hinnoittelijaMock);
        käsittelijä.käsittele(new Tilaus(asiakas, tuote));

        // Print
        System.out.println("Actual saldo: " + asiakas.getSaldo());
        System.out.println("Expected saldo: " + loppuSaldo);

        // Assert
        assertEquals(loppuSaldo, asiakas.getSaldo(), 0.001);
        verify(hinnoittelijaMock, times(2)).getAlennusProsentti(asiakas, tuote);
        verify(hinnoittelijaMock, times(1)).setAlennusProsentti(asiakas, alennus + 5);
    }

    @Test
    public void testPriceBelow100() {
        // Arrange
        float alkuSaldo = 100.0f;
        float listaHinta = 80.0f;
        float alennus = 5.0f;
        float loppuSaldo = alkuSaldo - (listaHinta * (1 - alennus / 100));
        Asiakas asiakas = new Asiakas(alkuSaldo);
        Tuote tuote = new Tuote("Below 100 Thing", listaHinta);

        // Record
        when(hinnoittelijaMock.getAlennusProsentti(asiakas, tuote))
                .thenReturn(alennus);

        // Act
        TilaustenKäsittely käsittelijä = new TilaustenKäsittely();
        käsittelijä.setHinnoittelija(hinnoittelijaMock);
        käsittelijä.käsittele(new Tilaus(asiakas, tuote));

        // Print
        System.out.println("Actual saldo: " + asiakas.getSaldo());
        System.out.println("Expected saldo: " + loppuSaldo);

        // Assert
        assertEquals(loppuSaldo, asiakas.getSaldo(), 0.001);
        verify(hinnoittelijaMock, times(2)).getAlennusProsentti(asiakas, tuote);
        verify(hinnoittelijaMock, times(0)).setAlennusProsentti(asiakas, alennus + 5);
    }
}
