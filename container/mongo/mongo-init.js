db = db.getSiblingDB("auth")
db.createUser({
  user: process.env.SERVICE_AUTH_DB_USER,
  pwd: process.env.SERVICE_AUTH_DB_PASSWORD,
  roles: [{ role: "readWrite", db: "auth" }],
});
db.createCollection("user")
db.user.createIndex({userId:1, oauthProvider:1})

db = db.getSiblingDB("parent")
db.createUser({
  user: process.env.SERVICE_PARENT_DB_USER,
  pwd: process.env.SERVICE_PARENT_DB_PASSWORD,
  roles: [{ role: "readWrite", db: "parent" }],
});

db = db.getSiblingDB("teacher")
db.createUser({
  user: process.env.SERVICE_TEACHER_DB_USER,
  pwd: process.env.SERVICE_TEACHER_DB_PASSWORD,
  roles: [{ role: "readWrite", db: "teacher" }],
});