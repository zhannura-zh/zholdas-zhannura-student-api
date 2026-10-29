# Assessment evidence

Student: Zhannura Zholdas, IT2-2306, 37742. Date: 30 October 2026.

- linux_administration.txt: actual Linux execution, chmod 744, backup comparison,
  Java process, last five log lines and surname search. The exercises run in an
  isolated Linux filesystem inside the local Jenkins container, not on NTFS.
- git_workflow.txt: original conflict resolution, personalized branches and commits.
- api_curl_results.json: real curl responses for CRUD, missing IDs, invalid input,
  duplicate email and the personalized application version.
- postgres_persistence.json: a named-volume record survived stopping and restarting
  both services. Only the test-created record was subsequently deleted.
- docker_container_inspect.json and docker_operations.txt: sanitized inspection,
  container logs and command execution. Secret environment values are excluded.
- jenkins_main_4*: initial corrected successful five-stage CI run.
- jenkins_failure_demo_1*: intentional failing test on the personal test branch.
  Later normal stages were skipped; the original console records the skip reason.
- jenkins_failure_demo_2*: repaired branch, all 16 normal tests passed.
- TEST-*.xml: actual local Gradle JUnit output; not fabricated test results.

The final main commit also demonstrates automatic SCM polling. Its build number,
source SHA and resulting image revision are recorded in the final assessment report.
The original release tag remains historical; the corrected release is v1.1.
