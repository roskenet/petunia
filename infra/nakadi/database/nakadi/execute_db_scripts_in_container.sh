find . -type f -name "*.sql" | sort |
while read -r filename; do PGPASSWORD=nakadi psql -h localhost -U "nakadi" -d "nakadi" -a -f "$filename"; done
