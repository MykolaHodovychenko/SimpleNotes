# Simple Notes

**Simple Notes** — навчальний Android-застосунок для створення, редагування, видалення та локального збереження нотаток.

Проєкт демонструє перехід від тимчасового збереження даних у `ViewModel` до повноцінного локального сховища на основі **Room + SQLite** з використанням шару **Repository**.

---

## Мета проєкту

Мета проєкту — показати базову архітектуру Android-застосунку на Jetpack Compose з локальним збереженням даних.

У проєкті реалізовано:

- два екрани: список нотаток і створення / редагування нотатки;
- навігацію між екранами;
- передачу параметра `id` при редагуванні нотатки;
- збереження нотаток у локальній базі даних Room;
- використання SQLite як вбудованої бази даних;
- шар Repository між `ViewModel` і DAO;
- UI State для керування станом екранів;
- кольорову диференціацію нотаток у списку.

---

## Функціональність застосунку

Користувач може:

- переглядати список нотаток;
- створювати нову нотатку;
- редагувати існуючу нотатку;
- видаляти нотатку;
- вибирати колір нотатки;
- бачити нотатки після повторного запуску застосунку.

Кожна нотатка має такі поля:

```kotlin
data class Note(
    val id: Int,
    val title: String,
    val text: String,
    val color: NoteColor
)
```

Доступні кольори нотаток:

```kotlin
enum class NoteColor {
    Yellow,
    Green,
    Blue,
    Pink,
    Purple,
    Orange
}
```

---

## Технології

У проєкті використано:

- Kotlin;
- Jetpack Compose;
- Material 3;
- Navigation Compose;
- ViewModel;
- StateFlow;
- Kotlin Coroutines;
- Room;
- SQLite;
- Repository pattern;
- KSP;
- Version Catalog `libs.versions.toml`.

---

## Архітектура проєкту

Проєкт побудований за простою багатошаровою схемою:

```text
UI
→ ViewModel
→ Repository
→ DAO
→ Room
→ SQLite
```

Зворотний потік даних:

```text
SQLite
→ Room
→ DAO
→ Repository
→ ViewModel
→ UI State
→ UI
```

UI не працює напряму з базою даних. Екрани отримують тільки готовий стан і callback-функції.

---

## Структура проєкту

```text
ua.opu.simplenotes
├── data
│   ├── local
│   │   ├── dao
│   │   │   └── NoteDao.kt
│   │   ├── database
│   │   │   └── AppDatabase.kt
│   │   └── entity
│   │       └── NoteEntity.kt
│   ├── mapper
│   │   └── NoteMapper.kt
│   └── repository
│       └── NoteRepository.kt
├── navigation
│   └── AppNavigation.kt
├── ui
│   ├── screens
│   │   ├── NotesListScreen.kt
│   │   └── NoteEditScreen.kt
│   ├── theme
│   └── viewmodel
│       ├── NotesViewModel.kt
│       └── NotesViewModelFactory.kt
└── MainActivity.kt
```

---

## Основні компоненти

### NoteEntity

`NoteEntity` описує таблицю нотаток у локальній базі даних.

```kotlin
@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val text: String,
    val color: String
)
```

### NoteDao

`NoteDao` містить методи для роботи з таблицею `notes`.

```kotlin
@Dao
interface NoteDao {

    @Query("SELECT * FROM notes ORDER BY id DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE id = :noteId")
    suspend fun getNoteById(noteId: Int): NoteEntity?

    @Insert
    suspend fun insertNote(note: NoteEntity)

    @Update
    suspend fun updateNote(note: NoteEntity)

    @Query("DELETE FROM notes WHERE id = :noteId")
    suspend fun deleteNoteById(noteId: Int)
}
```

### AppDatabase

`AppDatabase` створює Room-базу даних і надає доступ до `NoteDao`.

```kotlin
@Database(
    entities = [NoteEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "simple_notes_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
```

### NoteRepository

`NoteRepository` є проміжним шаром між `ViewModel` і DAO.

Repository:

- отримує список нотаток;
- отримує нотатку за `id`;
- створює нову нотатку;
- оновлює існуючу нотатку;
- видаляє нотатку;
- перетворює `NoteEntity` у `Note` і навпаки.

### NotesViewModel

`NotesViewModel` відповідає за підготовку стану для UI.

Вона:

- отримує список нотаток з Repository;
- формує `NotesListUiState`;
- зберігає тимчасовий стан форми редагування;
- викликає методи Repository для створення, редагування і видалення нотаток.

---

## Навігація

У проєкті використовується `Navigation Compose`.

Основні маршрути:

```kotlin
object AppRoutes {
    const val NOTES_LIST = "notes_list"
    const val NOTE_CREATE = "note_create"
    const val NOTE_EDIT = "note_edit/{noteId}"

    fun noteEdit(noteId: Int): String {
        return "note_edit/$noteId"
    }
}
```

Для створення нової нотатки використовується маршрут:

```text
note_create
```

Для редагування існуючої нотатки використовується маршрут з параметром:

```text
note_edit/{noteId}
```

Під час редагування між екранами передається тільки `id` нотатки:

```kotlin
navController.navigate(AppRoutes.noteEdit(noteId))
```

Повні дані нотатки завантажуються з Room через ViewModel і Repository.

---

## Локальне збереження даних

Після додавання Room нотатки зберігаються у локальній SQLite-базі даних.

Назва бази даних:

```text
simple_notes_database
```

Назва таблиці:

```text
notes
```

Структура таблиці:

```text
notes
├── id INTEGER PRIMARY KEY AUTOINCREMENT
├── title TEXT
├── text TEXT
└── color TEXT
```

Після закриття та повторного запуску застосунку нотатки залишаються у списку.

---

## Mapper

У проєкті використовується mapper для перетворення між моделлю бази даних і моделлю інтерфейсу.

```text
NoteEntity → Note
Note → NoteEntity
```

Це потрібно, тому що Room працює з `NoteEntity`, а UI працює з `Note`.

Наприклад, у базі даних колір зберігається як рядок:

```text
Blue
```

А в UI використовується enum:

```kotlin
NoteColor.Blue
```

---

## Підключення залежностей

У проєкті використовується `libs.versions.toml`.

Основні залежності:

```toml
[versions]
agp = "9.1.1"
kotlin = "2.2.10"
room = "2.8.4"
ksp = "2.2.10-2.0.2"
navigationCompose = "2.8.3"
composeBom = "2024.09.00"
```

Room:

```toml
androidx-room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "room" }
androidx-room-ktx = { group = "androidx.room", name = "room-ktx", version.ref = "room" }
androidx-room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "room" }
```

KSP:

```toml
[plugins]
ksp = { id = "com.google.devtools.ksp", version.ref = "ksp" }
```

У `build.gradle.kts` модуля `app`:

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}
```

Room-залежності:

```kotlin
implementation(libs.androidx.room.runtime)
implementation(libs.androidx.room.ktx)
ksp(libs.androidx.room.compiler)
```

---

## Особливість AGP 9 і KSP

Для проєкту з Android Gradle Plugin 9 може знадобитися додати в `gradle.properties`:

```properties
android.disallowKotlinSourceSets=false
```

Також у модулі `app` не потрібно окремо підключати:

```kotlin
alias(libs.plugins.kotlin.android)
```

оскільки в AGP 9 використовується built-in Kotlin.

Правильний варіант:

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}
```

---

## Запуск проєкту

1. Відкрити проєкт в Android Studio.
2. Дочекатися синхронізації Gradle.
3. Переконатися, що залежності Room і KSP підключені коректно.
4. Запустити застосунок на емуляторі або фізичному пристрої.
5. Створити кілька нотаток.
6. Закрити застосунок.
7. Запустити застосунок повторно.
8. Перевірити, що нотатки залишилися у списку.

---

## Перевірка бази даних

Дані можна перевірити через Android Studio:

```text
View → Tool Windows → App Inspection
```

Далі потрібно відкрити Database Inspector і знайти базу:

```text
simple_notes_database
```

У ній має бути таблиця:

```text
notes
```

---

## Типові проблеми

### Дані не зберігаються після перезапуску

Потрібно перевірити, що `ViewModel` більше не використовує локальний список:

```kotlin
private val _notes = MutableStateFlow(...)
```

Основним джерелом даних має бути:

```kotlin
repository.getAllNotes()
```

### Помилка KSP: `unexpected jvm signature V`

Потрібно оновити Room до актуальної версії:

```toml
room = "2.8.4"
```

### Помилка з `alias(libs.plugins.kotlin.android)`

Для AGP 9 потрібно прибрати:

```kotlin
alias(libs.plugins.kotlin.android)
```

і залишити:

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}
```

### Порожній список після першого запуску

Це нормально. Після переходу на Room демонстраційні нотатки зі списку у `ViewModel` більше не створюються автоматично.

Користувач має створити нові нотатки вручну. Після цього вони будуть збережені в локальній базі даних.

---

## Навчальне призначення проєкту

Цей проєкт можна використовувати для пояснення таких тем:

- Jetpack Compose;
- Navigation Compose;
- передача параметрів між екранами;
- ViewModel;
- UI State;
- StateFlow;
- Room;
- SQLite;
- DAO;
- Repository;
- mapper-функції;
- локальне збереження даних;
- розділення відповідальності між шарами застосунку.

---

## Підсумок

`Simple Notes` демонструє базовий, але повноцінний приклад Android-застосунку з локальним збереженням даних.

Проєкт показує, як перейти від тимчасового збереження нотаток у `ViewModel` до правильної архітектури з Room, Repository та SQLite.

Основний потік даних після завершення проєкту:

```text
UI
→ ViewModel
→ Repository
→ DAO
→ Room
→ SQLite
```

Застосунок зберігає нотатки між запусками, підтримує створення, редагування, видалення та кольорове відображення записів.
