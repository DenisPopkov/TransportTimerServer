package ru.popkov.transport.timer.server.application.repo

import com.mongodb.client.MongoDatabase
import data.Lesson
import data.Student
import io.kotest.core.Tuple2
import kotlinx.serialization.json.Json
import org.bson.Document
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection

private val client = KMongo.createClient("mongodb://root:example@127.0.0.1:27017")
private val mongoDatabase: MongoDatabase = client.getDatabase("admin")

private val student = mongoDatabase.getCollection<Map<String, Student>>().apply { drop() }
private val lessons = mongoDatabase.getCollection<Map<String, Lesson>>().apply { drop() }

val studentsRepo = MongoRepo<Student>(student)
val lessonsRepo = MongoRepo<Lesson>(lessons)

fun Document.toStudentMapper() = firstNotNullOf {
    Student(it.key, it.value.toString())
}

private val json = Json {
    ignoreUnknownKeys = true
}

fun createTestData() {
    listOf(
        Student("Sheldon", "Cooper"),
        Student("Leonard", "Hofstadter"),
        Student("Howard", "Wolowitz"),
        Student("Penny", "Hofstadter"),
    ).apply {
        map {
            studentsRepo.create(it)
        }
    }

    listOf(
        Lesson("Math"),
        Lesson("Phys"),
        Lesson("Story"),
    ).apply {
        map {
            lessonsRepo.create(it)
        }
    }

    val students = studentsRepo.read()
    val lessons = lessonsRepo.read()
//    val sheldon = students.findLast { it.elem.firstname == "Sheldon" }
//    check(sheldon != null)
//    val leonard = students.findLast { it.elem.firstname == "Leonard" }
//    check(leonard != null)
//    val math = lessons.findLast { it.elem.name =="Math" }
//    check(math != null)
//    val newMath = Lesson(
//        math.elem.name,
//        arrayOf(
//            GradeInfo(sheldon.id, Grade.A),
//            GradeInfo(leonard.id, Grade.B)
//        ))
//    lessonsRepo.update(math.id, newMath)
}
