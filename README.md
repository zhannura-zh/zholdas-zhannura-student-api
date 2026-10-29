# Student API by Zhannura Zholdas

DevOps Midterm Assessment. Group IT2-2306, student ID 37742.

Spring Boot 4.1.2, Java 17 bytecode, Gradle wrapper 9.8.2 and PostgreSQL 15.
Release 1.1.0 adds validated CRUD inputs, duplicate-email handling and protection
against overwriting existing records using a client-supplied POST ID.
These changes and their tests were developed on feature/zholdas-zhannura-crud.

## Start the application

Create a local `.env` from `.env.example`, then set your own `DB_PASSWORD`.
Do not commit the real `.env` or print `docker compose config` with live secrets.

```bash
docker compose build
docker compose up -d
docker compose ps
curl http://localhost:18080/api/students/info
curl http://localhost:18080/api/students/version
```

On Windows use `curl.exe` when PowerShell aliases `curl` to Invoke-WebRequest.
The database connection inside the application uses service name `postgres-db`
and container port 5432. The optional local database port is 5433.
The named volume `zholdas_postgres_data` preserves existing records. Stop the
stack with `docker compose down`; do not use `down -v` when data must remain.

## API

| Method | Route | Successful response |
|---|---|---|
| GET | /api/students/info | 200, student identity |
| GET | /api/students/version | 200, application version, name, group and ID |
| POST | /api/students | 201, created student |
| GET | /api/students | 200, student list |
| GET | /api/students/{id} | 200, student; 404 if missing |
| PUT | /api/students/{id} | 200, updated student; 404 if missing |
| DELETE | /api/students/{id} | 204; 404 if missing |

Request example: `{"name":"Zhannura Zholdas","email":"zhannura@example.test"}`.
Name must be nonblank and at most 100 characters. Email must be valid, nonblank,
unique and at most 254 characters. Invalid input returns 400; duplicate email
returns 409. POST ignores a supplied ID and always creates a new record.

## Build and automated tests

```bash
./gradlew clean assemble
./gradlew test
```

Windows: use `./gradlew.bat`. The suite runs against isolated H2 in PostgreSQL
compatibility mode; it does not delete or modify development PostgreSQL records.
Real PostgreSQL CRUD and restart persistence are verified separately.
JUnit XML is generated under `build/test-results/test/`.

## Linux administration

Use a Linux filesystem for full owner/group/other permission semantics.

```bash
mkdir -p scripts logs backups src
chmod 744 scripts/Zholdas_Zhannura_system_report.sh
./scripts/Zholdas_Zhannura_system_report.sh
ls -l scripts/Zholdas_Zhannura_system_report.sh
cp README.md backups/README_Zholdas_Zhannura.md
cmp README.md backups/README_Zholdas_Zhannura.md
ps aux
pgrep -af java
tail -n 5 logs/Zholdas_Zhannura_system_report.log
grep Zholdas logs/Zholdas_Zhannura_system_report.log > Zholdas_Zhannura_log_analysis.txt
```

`>` replaces a file's contents; `>>` appends to it. The monitoring script resolves
paths relative to itself and records identity, time, OS/kernel, CPU, memory,
disk usage and available space. The required mode 744 is owner rwx, group r--,
others r--; Git retains the executable bit but not all Linux mode bits.

## Git workflow

The history retains the personalized initial commit, the CRUD feature branch,
the documentation branch and its manually resolved README conflict. Additional
CRUD code and test commits complete the original documentation-only feature.

```bash
git remote -v
git log --oneline --graph --all
git branch -av
git tag -n
```

The original `v1.0-zholdas-zhannura` tag is preserved. The corrected release uses
`v1.1-zholdas-zhannura`, also used as the Docker image version tag.
Passwords previously committed in the original history are obsolete: the local
lab database password has been rotated. History is retained rather than rewritten.

## Jenkins

Job: `Zholdas_Zhannura_Student_API`, local URL `http://localhost:8088/`.
The Pipeline reads the standard `Jenkinsfile` from this repository's main branch.
The `zholdas-docker` node has Java, Git, the Gradle wrapper and Docker CLI access.
See `infra/jenkins/` for its reproducible container configuration. Docker socket
access is privileged and is limited to this authorized local laboratory node.

Stages: **Checkout, Build, Test, Docker Build, Verify**. Test failures stop the
normal flow. Docker build errors propagate. Verify inspects the actual image
and compares its revision label with the checked-out commit. The image is
`zholdas-zhannura-student-api:v1.1-zholdas-zhannura`.

SCM polling runs every two minutes. Once the job has run with this Jenkinsfile,
a later pushed main commit starts a build automatically on the next poll.
The separate personal CI failure-demo branch/job demonstrates a deliberately
failing test and the subsequent fix; that error is never merged into main.

CI automatically integrates, builds and tests changes. Continuous Delivery keeps
a verified result ready for release. Continuous Deployment also automatically
releases each accepted change. This assessment requires CI and image verification;
production deployment is not required and this Pipeline does not perform it.

## Evidence

The `evidence/` directory contains actual command outputs and verification results.
The assessment report records the final source revision and Jenkins build numbers.
Never include `.env`, Jenkins authentication files or private database backups in
submission archives.

This Windows host already runs Apache on port 8080. The local .env maps the API to 127.0.0.1:18080; the container still listens on 8080.
