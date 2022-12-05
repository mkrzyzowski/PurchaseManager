# Informacje wstępne

Większość rozbudowanych systemów informatycznych korzysta z danych utrwalonych w relacyjnych bazach danych. Biorąc pod uwagę fakt, że tematyka baz danych nie jest częścią realizowanych zajęć z programowania - zaimplementowano autorskie rozwiązanie korzystające z JDBC i umożliwiające realizację podstawowych operacji na danych - tworzenie, pobieranie, modyfikację i usuwanie (ang. _CRUD_). Z punktu widzenia programistów niewidoczne będą elementy bezpośrednio związane z bazą danych.  

W dokumentacji przedstawiono opis i przykłady wykonywania w zalecany sposób podstawowych operacji na danych w programie `Purchase Manager`.

# 1. Tworzenie klas / tabel

Aby odwzorować w aplikacji byt zapisywalny w bazie danych, należy przygotować odpowiednią klasę. Klasa te powinna spełniać poniższe zasady:
- klasa powinna być umieszczona w pakiecie `model`
- klasa musi dziedziczyć po klasie `BaseEntity`
- nazwa klasy powinna być identyczna jak nazwa tabeli bazodanowej
- w klasie powinny znajdować się wyłącznie pola związane z kolumnami bazy danych
- stosowane powinny być obiektowe typy pól (tj. `int` -> `Integer`, `double` -> `Double`, `boolean` -> `Boolean`)
- należy przestrzegać hermetyzacji stosując pola prywatne oraz gettery/settery powiązane z tymi polami

### Przykład
```java
public class Student extends BaseEntity {
    private String firstName;
    private String lastName;
    private LocalDateTime dateOfBirth;
    private Integer growth;
    private Double weight;

    // ... konstruktory, gettery settery
}
```

# 2. Pobieranie danych

W chwili obecnej istnieje kilka sposobów pobierania danych z systemu. Wszystkie one wymagają wykorzystania odpowiedniej metody statycznej z klasy `DatabaseManager`.  
**UWAGA!** Podczas pobierania jednego obiektu w przypadku, gdy w bazie danych nie ma odpowiednich danych spełniających określone warunki - zwracana jest wartość `null`. W przypadku pobierania listy obiektów i braku obiektów spełniających kryteria - zwracana jest pusta lista.

## 2.1. Pobieranie obiektu klasy na podstawie identyfikatora

Z tego sposobu należy korzystać, kiedy wiadomo, jaki jest identyfikator obiektu i do jakiej klasy on należy.  
Przykład użycia:
```java
Student student = DatabaseManager.getById(Student.class, 1);
```

## 2.2. Pobieranie wszystkich obiektów danej klasy

Jeśli chcemy pobrać dane wszystkich obiektów danej klasy - ten sposób jest najlepszym wyjściem.  
Przykład użycia:
```java
List<Student> students = DatabaseManager.getAll(Student.class);
```

## 2.3. Pobieranie wszystkich obiektów danej klasy utworzonych przez zalogowanego użytkownika

Aby pobrać wszystkie obiekty danej klasy, które utworzył zalogowany użytkownik - należy skorzystać z tego sposobu.  
Przykład użycia:
```java
List<Student> students = DatabaseManager.getAllByInsertUserId(Student.class);
```

## 2.4. Pobieranie jednego obiektu danej klasy spełniającego określone warunki

Z tego sposobu korzystamy, aby pobrać jeden obiekt danej klasy spełniający podane warunki.  
Przykład użycia:
```java
List<Pair<String, Object>> params = new ArrayList<>();
params.add(new Pair<>("fistName", "Mariusz")); // pole z klasy : wartość
params.add(new Pair<>("weight", 81.5)); // pole z klasy : wartość
Student student = DatabaseManager.getOne(Student.class, params);
```

## 2.5. Pobieranie jednego obiektu danej klasy spełniającego określone warunki, z dodatkowym zapytaniem

Z tego sposobu korzystamy, aby pobrać jeden obiekt danej klasy spełniający podane warunki. Ponadto przekazywany jest obiekt zapytania zdefiniowany przez użytkownika.  
Przykład użycia:
```java
Query query = new Query();
query.setSqlOrderBy("firstName DESC"); // imiona sortowane malejąco
List<Pair<String, Object>> params = new ArrayList<>();
params.add(new Pair<>("fistName", "Mariusz")); // pole z klasy : wartość
params.add(new Pair<>("weight", 81.5)); // pole z klasy : wartość
Student student = DatabaseManager.getOne(Student.class, params, query);
```

## 2.6. Pobieranie listy obiektów danej klasy spełniających określone warunki

Z tego sposobu korzystamy, aby pobrać jeden lub wiele obiektów danej klasy spełniających podane warunki.  
Przykład użycia:
```java
List<Pair<String, Object>> params = new ArrayList<>();
params.add(new Pair<>("weight", 81.5)); // pole z klasy : wartość
List<Student> students = DatabaseManager.getList(Student.class, params);
```

## 2.7. Pobieranie listy obiektów danej klasy spełniających określone warunki, z dodatkowym zapytaniem

Z tego sposobu korzystamy, aby pobrać jeden lub wiele obiektów danej klasy spełniających podane warunki.  
Ponadto przekazywany jest obiekt zapytania zdefiniowany przez użytkownika.  
Przykład użycia:
```java
Query query = new Query();
query.setSqlOrderBy("firstName DESC"); // imiona sortowane malejąco
List<Pair<String, Object>> params = new ArrayList<>();
params.add(new Pair<>("weight", 81.5)); // pole z klasy : wartość
List<Student> students = DatabaseManager.getList(Student.class, params, query);
```

# 3. Tworzenie danych

W celu utworzenia i zapisania danych w bazie - należy przygotować i uzupełnić wartościami obiekt danej klasy, a następnie wywołać na nim operację `insert()`. Informacje o dacie i autorze nowego rekordu (kolumny `InsertDate` oraz `InsertUserId`) zostaną uzupełnione automatycznie. W wyniku operacji uzyskujemy identyfikator utworzonego rekordu lub wartość `null` - jeśli operacja się nie powiodła.    
Przykład:
```java
Student student = new Student();
student.setFirstName("Mariusz");
student.setLastName("Nowak");
student.setDateOfBirth(LocalDateTime.now());

Integer newRecordId = student.insert(); // zapis rekordu w bazie danych
```

# 4. Modyfikacja danych

W celu zmodyfikowania danych w bazie - należy pobrać odpowiedni obiekt, a następnie zmienić jego dane i wywołać na obiekt operację `modify()`. Informacje o dacie i autorze modyfikacji rekordu (kolumny `ModifyDate` oraz `ModifyUserId` zostaną uzupełnione automatycznie. Metoda `modify` zwraca wartość `true` - jeśli operacja się powiodła lub `false` - jeśli nie udało się zapisać obiektu.  
Przykład:
```java
...
        
student.setFirstName("Janusz");
student.setDateOfBirth(LocalDateTime.now());

boolean result = student.update(); // aktualizacja rekordu w bazie danych
```

# 5. Usuwanie danych

W celu usunięcia danych z bazy danych, na pobranym obiekcie należy wywołać metodę `delete()`. Metoda ta zwraca wartość `true` - jeśli operacja się powiodła lub `false` - jeśli nie udało się usunąć obiektu. **UWAGA!** Po usunięciu obiektu z bazy aktualny obiekt Java w dalszym ciągu będzie posiadał uzupełnione wartości.  
Przykład:
```java
...
boolean result = student.delete(); // usunięcie rekordu z bazy danych
```
