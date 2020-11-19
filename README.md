# yelp-app

## About the project

The project is a data analysis application for Yelp.com's business review data. The project includes two parts. The first part generates the yelp data. The sceond part is the application to search data.

## Getting Started

### Prerequisites

You need to have the following software installed and data downloaded:

1. Java
2. Docker
3. Yelp Dataset (<https://www.yelp.com/dataset/challenge>)

### Installation

1. Install Oracle Standard Edition 12c Release 2 (Follow <https://github.com/MaksymBilenko/docker-oracle-12c>)

2. Connect to the database and execute createdb.sql to generate tables.

3. Build Maven project

```sh
./mvnw clean install
```

The command will generate .jar files in the target file.

4. Populate dataset. Go to the target file and execute the following command.

```sh
java -jar populate.jar yelp_business.json yelp_review.json yelp_checkin.json yelp_user.json
```

The arguments are the dataset json files.

5. Execute application.jar to start your search!
