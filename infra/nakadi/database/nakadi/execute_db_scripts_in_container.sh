find . -type f -name "*.sql" | sort |
while read -r filename; do PGPASSWORD=nakadi psql -h localhost -p 6543 -U "nakadi" -d "nakadi" -a -f "$filename"; done
