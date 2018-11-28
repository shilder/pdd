# java.ural.Meetup #2 talk notes

Здесь исходники чата, который я показывал на java.ural.Meetup в рамках доклада
по Clojure. Для правильной работы требуется бинарник
[MyStem](https://tech.yandex.ru/mystem/) в текущем каталоге.

## Ссылки на русскоязычные ресурсы ##

Telegram канал русскоязычного сообщества Clojure-разработчиков
https://t.me/clojure_ru

Clojure Ekaterinburg на meetup.com
https://www.meetup.com/ru-RU/Clojure-Ekaterinburg/

## Ссылки из презентации ##

* [Ссылка на презентацию](https://docs.google.com/presentation/d/1ZqAVoInJlXpefpHCVMYtyA5b9b9sQSPkgoTZTrpFgXU/edit?usp=sharing)
* https://thenewstack.io/the-new-stack-makers-adrian-cockcroft-on-sun-netflix-clojure-go-docker-and-more/
* https://blog.cognitect.com/blog/2015/6/30/walmart-runs-clojure-at-scale
* [Clojure in Netflix (HN Discussion)](https://news.ycombinator.com/item?id=18345243)
* https://jobs.apple.com/en-us/details/114424334/senior-clojure-software-engineer
* [Teaching Clojure at IBM - Steve Shogren](https://www.youtube.com/watch?v=BsLiPt90HDo)
* https://thoughtworks.github.io/p2/issue09/two-months-early/
* https://puppetlabs.com/blog/introducing-puppetdb-put-your-data-to-work
* [Realtime Collaboration with Clojure - Leonardo Borges](https://www.youtube.com/watch?v=3QR8meTrh5g)
* https://clojure.org/community/success_stories
* https://www.reddit.com/r/Clojure/comments/68r4lz/one_of_facebook_projects_migrating_from_clojure/
* [Comment from discussion on HN about Amazon abandoning Clojure projects](https://news.ycombinator.com/item?id=18346154)
* [Techempower Round 17 JSON serialization benchmark](https://www.techempower.com/benchmarks/#section=data-r17&hw=ph&test=json)
* https://snyk.io/blog/jvm-ecosystem-report-2018
* https://redmonk.com/sogrady/2018/08/10/language-rankings-6-18/
* https://github.com/shilder/jum
* https://t.me/clojure_ru

## Где узнать больше про Clojure ? ##

Официальный сайт: https://clojure.org

### Вообще про Lisp ###

Хорошая статья по поводу того, зачем в лиспе все эти скобки:
[The Nature of Lisp](http://www.defmacro.org/ramblings/lisp.html)

Своего рода классическая статья от Paul Graham про Lisp, про скобки и прочее
[Beating the averages](http://www.paulgraham.com/avg.html)

### Обзоры Clojure ###

Эти видео довольно старые, но тем не менее они нисколько не потеряли актуальности

[The Curious Clojurist with Neal Ford](https://www.youtube.com/watch?v=bxLnpgnDApg)
Хороший обзор Clojure с примерами кода, рассказом про особенности многопоточного
программирования, немного метапрограммирования. (2012 год)

[Clojure for Java Programmers Part 1 - Rich Hickey](https://www.youtube.com/watch?v=P76Vbsk_3J0)
Довольно подробное введение от автора языка, где он объясняет какие есть структуры
данных и как это все работает, в формате ориентированном на Java-разработчика (2012 год)

### Философские и архитектурные решения от Rich Hickey ###

Рекомендуются к просмотру чтобы понять идеи и архитектуру языка. Опять таки, видео
уже старые, но все идеи которые в них озвучены актуальны и сейчас

[Simple made easy](https://www.infoq.com/presentations/Simple-Made-Easy)
Классика Rich Hickey. Объясняются идеи стоящие за Simple, отличия
simple от easy и способы построения этих самых simple систем (2011 год).

[Rails Conf 2012 Keynote: Simplicity Matters by Rich Hickey](https://www.youtube.com/watch?v=rI8tNMsozo0)
Тоже самое что и предыдущий доклад, но немного по-другому сформулировано (2012 год)

[The Language of the System - Rich Hickey](https://www.youtube.com/watch?v=ROor6_NGIWU)
Доклад про особенности построения систем (2013 год)

### Метапрограммирование в Lisp и Clojure ###

Видео от Timothy Baldridge по поводу макросов в Clojure
[Clojure: Deep Walking Macros](https://www.youtube.com/watch?v=HXfDK1OYpco)

### Протоколы и мультиметоды ###

Неплохая статья где рассказывается про то, какую проблему решают протоколы и мультиметоды
[Solving the Expression Problem with Clojure 1.2](https://www.ibm.com/developerworks/library/j-clojure-protocols)

Статья, в которой объясняются отличия протоколов, мультиметодов, switch и if/else с точки зрения производительности
[Polymorphic performance](http://insideclojure.org/2015/04/27/poly-perf/)

### Persistent Data Structures ###

Видео от Rich Hickey с объяснением встроенных структур данных
[Clojure Data Structures Part 1 - Rich Hickey](https://www.youtube.com/watch?v=ketJlzX-254)

Статья, на основании которой были реализованы структуры данных для Clojure (и для Scala):
[Ideal Hash Trees](http://lampwww.epfl.ch/papers/idealhashtrees.pdf)

Цикл статей с подробным объяснением структуры Persistent Vector в Clojure:

* [Part 1](http://www.hypirion.com/musings/understanding-persistent-vector-pt-1)
* [Part 2](http://www.hypirion.com/musings/understanding-persistent-vector-pt-2)
* [Part 3](http://www.hypirion.com/musings/understanding-persistent-vector-pt-3)
* [Transients](http://www.hypirion.com/musings/understanding-clojure-transients)
* [Performance benchmarks](http://www.hypirion.com/musings/persistent-vector-performance-summarised)

Альтернативная реализация Persistent Data Structures для Java
[Bifurcan](https://github.com/lacuna/bifurcan)

### Различные ссылки ###

[Design patterns на clojure](http://mishadoff.com/blog/clojure-design-patterns/)
Список типичных ООП паттернов и примеры как их можно реализовать на Clojure.

[Thinking in Data](https://www.infoq.com/presentations/Thinking-in-Data)
Доклад по поводу разработки с использованием данных
