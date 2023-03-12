# Company defense

Documenation in progress.

## Formatting

```bash
sbt scalafmtAll
```

## Linting

Wartremover is run on save. `build.sbt` contains the configuration for the linter.
Please see here to set more rules: <https://www.wartremover.org/doc/warts.html>.


## Scala 3 migration

## Resources
### Maps
### WaveData
Contains information about the upcoming enemies. One row is one wave. So basically it has a list of enemies. Next wave starts after the previous one has been done, and enemies will spawn in predetermined times. Thus, only enemy data without time information is saved in the file. If we run out of waves, a new wave is generated based on the last destroyed wave. Meaning of letters in the rows:
- B: Basic enemy (IBM)
- S: Splitting enemy (Google)
- T: Tank enemy (Twitter)
- C: Camouflaged enemy (TSMC)
- F: Flock enemy (Huawei)
