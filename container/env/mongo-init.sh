set -e

mongo <<EOF
db = db.getSiblingDB('auth')
db.createUser({
  user: '$SERVICE_AUTH_DB_USER',
  pwd: '$SERVICE_AUTH_DB_PASSWORD',
  roles: [{ role: 'readWrite', db: 'auth' }],
});
db.createCollection('users')

db = db.getSiblingDB('parent')

db.createUser({
  user: '$SERVICE_PARENT_DB_USER',
  pwd: '$SERVICE_PARENT_DB_PASSWORD',
  roles: [{ role: 'readWrite', db: 'parent' }],
});
db.createCollection('users')

EOF