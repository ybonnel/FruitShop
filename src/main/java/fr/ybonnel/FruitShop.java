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

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class FruitShop {

    private static class Article {

        private int price;
        private int lotSize;
        private int reducPrice;
        private String cat;

        public Article(int price, int lotSize, int reducPrice, String cat) {
            this.price = price;
            this.lotSize = lotSize;
            this.reducPrice = reducPrice;
            this.cat = cat;
        }
    }

    private ConcurrentHashMap<String, Article> database = new ConcurrentHashMap<>();

    public FruitShop() {
        database.put("Pommes", new Article(100, 1, 0, "P"));
        database.put("Mele", new Article(100, 2, 100, "P"));
        database.put("Apples", new Article(100, 3, 100, "P"));
        database.put("Bananes", new Article(150, 2, 150, "B"));
        database.put("Cerises", new Article(75, 2, 20, "C"));
    }

    public static void main(String[] args) {
        new FruitShop().caisse(System.in, System.out);

    }

    private ConcurrentHashMap<String, AtomicInteger> counters = new ConcurrentHashMap<>();

    public void caisse(InputStream in, PrintStream out) {

        out.println("Caisse prête...");

        Scanner scanner = new Scanner(in);

        AtomicInteger sum = new AtomicInteger(0);

        while (true) {
            String article = scanner.next();

            if (article.equals("exit")) {
                break;
            }

            out.println(sum.addAndGet(Stream.of(article.split(","))
                    .map(this::traiteArticle)
                    .mapToInt(Integer::valueOf)
                    .sum()));

        }

        out.println("Good bye");
    }

    private int traiteArticle(String articleName) {
        Article article = database.get(articleName);

        int counter = counters.computeIfAbsent(articleName, (key) -> new AtomicInteger(0)).incrementAndGet();
        int counterCat = counters.computeIfAbsent(article.cat, (key) -> new AtomicInteger(0)).incrementAndGet();
        int counterFruit = counters.computeIfAbsent("Fruit", (key) -> new AtomicInteger(0)).incrementAndGet();

        int articlePrice = article.price;

        if (counter == article.lotSize) {
            counters.get(articleName).set(0);
            articlePrice -= article.reducPrice;
        }

        if (article.cat.equals("P") && counterCat == 4) {
            articlePrice -= 100;
            counters.get(article.cat).set(0);
        }
        if (counterFruit == 5) {
            articlePrice -= 200;
            counters.get("Fruit").set(0);

        }
        return articlePrice;

    }
}
