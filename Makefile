build:
	docker compose run --rm app ./gradlew build

tasks:
	docker compose run --rm app ./gradlew tasks

jar:
	docker compose run --rm app ./gradlew jar

exercici1:
	docker compose run --rm app java -cp "/app/build/classes/java/main:/app/build/resources/main:/app/.gradle/caches/modules-2/files-2.1/org.postgresql/postgresql/42.3.1/9ca7df660e875b91c78e3d1608d4d7469ad3470c/postgresql-42.3.1.jar" edu.uoc.practica.bd.uocdb.exercise1.Exercise1PrintReportOverQuery

exercici2:
	docker compose run --rm app java -cp "/app/build/classes/java/main:/app/build/resources/main:/app/.gradle/caches/modules-2/files-2.1/org.postgresql/postgresql/42.3.1/9ca7df660e875b91c78e3d1608d4d7469ad3470c/postgresql-42.3.1.jar" edu.uoc.practica.bd.uocdb.exercise2.Exercise2InsertAndUpdateDataFromFile

reset:
	docker compose down -v
	docker compose up -d db
	sleep 5
	docker compose exec db psql -U postgres -d postgres -f /scripts/create_db.sql
	docker compose exec db psql -U postgres -d postgres -f /scripts/inserts_db.sql
	docker compose exec db psql -U postgres -d postgres -f /scripts/create_view.sql
	docker compose exec db psql -U postgres -d postgres -f /scripts/create_function.sql
