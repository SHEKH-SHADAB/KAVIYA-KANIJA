package com.example.kavyakanija.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.kavyakanija.data.model.Poem
import com.example.kavyakanija.data.model.Poet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Database(entities = [Poet::class, Poem::class], version = 5, exportSchema = false)
abstract class KavyaKanijaDatabase : RoomDatabase() {

    abstract fun poetryDao(): PoetryDao

    companion object {
        @Volatile
        private var INSTANCE: KavyaKanijaDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): KavyaKanijaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KavyaKanijaDatabase::class.java,
                    "kavya_kanija_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                
                // Trigger population check after build
                scope.launch(Dispatchers.IO) {
                    checkAndPopulate(instance.poetryDao())
                }
                
                instance
            }
        }

        private suspend fun checkAndPopulate(poetryDao: PoetryDao) {
            val poems = poetryDao.getAllPoems().first()
            if (poems.isEmpty()) {
                populateDatabase(poetryDao)
            }
        }

        private suspend fun populateDatabase(poetryDao: PoetryDao) {
            val poets = listOf(
                Poet(id = 1, name = "ಕುವೆಂಪು (Kuvempu)", era = "ನವೋದಯ", bio = "ಕುಪ್ಪಳಿ ವೆಂಕಟಪ್ಪ ಪುಟ್ಟಪ್ಪ ಕನ್ನಡದ ಅಗ್ರಗಣ್ಯ ಕವಿ.", imageUrl = null),
                Poet(id = 2, name = "ದ.ರಾ. ಬೇಂದ್ರೆ (D.R. Bendre)", era = "ನವೋದಯ", bio = "ದತ್ತಾತ್ರೇಯ ರಾಮಚಂದ್ರ ಬೇಂದ್ರೆ ವರಕವಿ ಎಂದೇ ಪ್ರಖ್ಯಾತರು.", imageUrl = null),
                Poet(id = 3, name = "ಡಿ.ವಿ. ಗುಂಡಪ್ಪ (D.V.G)", era = "ನವೋದಯ", bio = "ದೇವನಹಳ್ಳಿ ವೆಂಕಟರಮಣಯ್ಯ ಗುಂಡಪ್ಪ ಪ್ರಸಿದ್ಧ ಕನ್ನಡ ಸಾಹಿತಿ.", imageUrl = null),
                Poet(id = 4, name = "Robert Frost", era = "Modern", bio = "An American poet known for his realistic depictions of rural life.", imageUrl = null),
                Poet(id = 5, name = "William Wordsworth", era = "Romantic", bio = "A major English Romantic poet.", imageUrl = null)
            )
            poetryDao.insertPoets(poets)

            val poemsList = listOf(
                Poem(
                    id = 1, poetId = 1, title = "ಓ ನನ್ನ ಚೇತನ", 
                    content = "ಓ ನನ್ನ ಚೇತನ, ಆಗು ನೀ ಅನಿಕೇತನ!\n\nರೂಪ ರೂಪಗಳನು ದಾಟಿ, ನಾಮ ನಾಮಗಳನು ಮೀಟಿ,\nಅಣುಕ್ಷಣ, ಅಣುಕ್ಷಣ, ಆಗು ನೀ ಅನಿಕೇತನ!", 
                    genre = "ದಾರ್ಶನಿಕ", categoryColor = "#FF5722",
                    titleEnglish = "O My Spirit",
                    contentEnglish = "O my spirit, become unhoused!\n\nBeyond forms, beyond names,\nEvery moment, every second, become unhoused!",
                    audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
                ),
                Poem(
                    id = 2, poetId = 2, title = "ಬಾರೋ ಸಾಧನಕೇರಿಗೆ", 
                    content = "ಬಾರೋ ಸಾಧನಕೇರಿಗೆ, ಈ ಮರ-ಗಿಡಗಳಿಗೆ,\nಈ ಹಳ್ಳ-ಕೊಳ್ಳಗಳಿಗೆ, ಬಾರೋ ಸಾಧನಕೇರಿಗೆ.", 
                    genre = "ಪ್ರಕೃತಿ", categoryColor = "#2E7D32",
                    titleEnglish = "Come to Sadhanakeri",
                    contentEnglish = "Come to Sadhanakeri, to these trees and plants,\nTo these streams and valleys, come to Sadhanakeri.",
                    audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3"
                ),
                Poem(
                    id = 3, poetId = 3, title = "ಮಂಕುತಿಮ್ಮನ ಕಗ್ಗ", 
                    content = "ಹುಲ್ಲಾಗು ಬೆಟ್ಟದಡಿ, ಮನೆಗೆ ಮಲ್ಲಿಗೆಯಾಗು,\nಕಲ್ಲಾಗು ಕಷ್ಟಗಳ ಮಳೆಯು ಸುರಿಯಲು.\nಬೆಲ್ಲವಾಗು ದೀನ ದಲಿತರ ಪಾಲಿಗೆ,\nಎಲ್ಲರೊಳಗೊಂದಾಗು ಮಂಕುತಿಮ್ಮ.", 
                    genre = "ದಾರ್ಶನಿಕ", categoryColor = "#FF5722",
                    titleEnglish = "Mankuthimmana Kagga",
                    contentEnglish = "Be like the grass at the foot of the hill, a jasmine to the home,\nBe like a stone when the rain of difficulties pours.\nBe like jaggery to the poor and downtrodden,\nBe one among all, Mankuthimma.",
                    audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3"
                ),
                Poem(
                    id = 4, poetId = 1, title = "ಜಯ ಭಾರತ ಜನನಿಯ ತನುಜಾತೆ", 
                    content = "ಜಯ ಭಾರತ ಜನನಿಯ ತನುಜಾತೆ, ಜಯ ಹೇ ಕರ್ನಾಟಕ ಮಾತೆ!\nಜಯ ಸುಂದರ ನದಿ ವನಗಳ ನಾಡೇ, ಜಯ ಹೇ ರಸಋಷಿಗಳ ಬೀಡೇ!", 
                    genre = "ದೇಶಭಕ್ತಿ", categoryColor = "#FFB300",
                    titleEnglish = "Victory to Daughter of Mother India",
                    contentEnglish = "Victory to the daughter of Mother India, victory to Mother Karnataka!\nVictory to the land of beautiful rivers and forests, victory to the abode of poet-saints!",
                    audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3"
                ),
                Poem(
                    id = 5, poetId = 4, title = "The Road Not Taken", 
                    content = "Two roads diverged in a yellow wood,\nAnd sorry I could not travel both\nAnd be one traveler, long I stood\nAnd looked down one as far as I could\nTo where it bent in the undergrowth;", 
                    genre = "Philosophical", categoryColor = "#FF5722",
                    audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-5.mp3"
                ),
                Poem(
                    id = 6, poetId = 5, title = "Daffodils", 
                    content = "I wandered lonely as a cloud\nThat floats on high o'er vales and hills,\nWhen all at once I saw a crowd,\nA host, of golden daffodils;\nBeside the lake, beneath the trees,\nFluttering and dancing in the breeze.", 
                    genre = "Nature", categoryColor = "#4CAF50",
                    audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-6.mp3"
                )
            )
            poetryDao.insertPoems(poemsList)
        }
    }
}
