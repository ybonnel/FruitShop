/*
 * Copyright 2013- Yan Bonnel
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.ybonnel;

import org.apache.commons.io.input.ReaderInputStream;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class FruitShopTest {

    private void oneTest(int expectedResult, String... articles) {
        InputStream reader = new ReaderInputStream(new StringReader(
                Stream.of(articles).collect(Collectors.joining("\n")) + "\nexit\n"
        ));

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        new FruitShop().caisse(reader, new PrintStream(stream));

        String result = new String(stream.toByteArray());

        String[] lines = result.split("\\n");

        String lastTotal = lines[lines.length - 2];

        System.out.println(result);

        assertEquals(expectedResult, Integer.parseInt(lastTotal));


    }

    @Test
    public void testeSimpleCaisse() {

        oneTest(410, "Cerises", "Pommes", "Cerises", "Bananes", "Cerises", "Cerises", "Pommes");

    }


    @Test
    public void testeCsv() {
        oneTest(410, "Cerises,Pommes,Cerises", "Bananes", "Cerises,Cerises", "Pommes");
    }

    @Test
    public void testeBananes() {


        oneTest(355, "Cerises", "Pommes", "Cerises", "Bananes", "Pommes", "Bananes", "Cerises");
    }

    @Test
    public void testeMultiLang() {


        oneTest(380, "Cerises", "Apples", "Cerises", "Bananes", "Pommes", "Mele");
    }

    @Test
    public void testeMultiLangPlus() {


        oneTest(230, "Mele", "Apples", "Apples", "Pommes", "Apples", "Mele", "Cerises", "Cerises");
    }

    @Test
    public void testeMultiLangPlusPlus() {


        oneTest(380, "Mele", "Apples", "Apples", "Pommes", "Apples", "Mele", "Cerises", "Cerises", "Bananes");
    }

    @Test
    public void testeMultiLangPlusPlusPlus() {


        oneTest(380, "Cerises,Apples", "Cerises", "Apples,Pommes,Bananes", "Apples,Pommes","Mele", "Pommes");
    }

    @Test
    public void testeApoteose() {


        oneTest(150, "Mele,Apples,Apples,Mele", "Bananes", "Mele,Apples,Apples,Pommes,Mele");
    }

    @Test
    public void testeApoteosePlus() {


        oneTest(250, "Mele,Apples,Apples,Pommes,Mele", "Bananes");
    }
}
