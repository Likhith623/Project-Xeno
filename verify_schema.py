import os
import re

SQL_FILE = r"C:\Users\Sarishma\Project-Xeno\database.sql"
JPA_DIR = r"C:\Users\Sarishma\Project-Xeno\xenon-backend\src\main\java\com\xenocrm"

# Extract SQL Tables and Columns
sql_tables = {}
with open(SQL_FILE, 'r', encoding='utf-8') as f:
    sql_content = f.read()

# Very basic regex parsing for CREATE TABLE
table_blocks = re.findall(r"CREATE TABLE ([a-z_]+)\s*\((.*?)\);", sql_content, re.IGNORECASE | re.DOTALL)
for table_name, columns_str in table_blocks:
    columns = {}
    lines = columns_str.split('\n')
    for line in lines:
        line = line.strip()
        if not line or line.startswith('--') or line.startswith('CONSTRAINT') or line.startswith('PRIMARY KEY') or line.startswith('FOREIGN KEY') or line.startswith('UNIQUE'):
            continue
        parts = line.split()
        if len(parts) >= 2:
            col_name = parts[0]
            col_type = parts[1]
            columns[col_name.lower()] = col_type.upper()
    sql_tables[table_name.lower()] = columns

# Extract JPA Entities
jpa_entities = {}
for root, _, files in os.walk(JPA_DIR):
    for file in files:
        if file.endswith("Entity.java"):
            with open(os.path.join(root, file), 'r', encoding='utf-8') as f:
                content = f.read()
                
            table_match = re.search(r'@Table\(name\s*=\s*"([a-z_]+)"\)', content)
            if table_match:
                table_name = table_match.group(1).lower()
                
                # Extract @Column
                columns = {}
                col_matches = re.finditer(r'@Column\(.*?(?:name\s*=\s*"([a-z_]+)").*?\)\s*(?:private|protected)\s+([A-Za-z0-9_<>]+)\s+([A-Za-z0-9_]+)', content)
                for match in col_matches:
                    columns[match.group(1).lower()] = match.group(2)
                
                # Extract simple private fields (often assumed to be columns if no @Transient)
                field_matches = re.finditer(r'(?!.*@Transient)^\s*(?:@.*?\s+)*private\s+([A-Za-z0-9_<>]+)\s+([A-Za-z0-9_]+);', content, re.MULTILINE)
                for match in field_matches:
                    java_type = match.group(1)
                    field_name = match.group(2)
                    # Convert camelCase to snake_case
                    col_name = re.sub(r'(?<!^)(?=[A-Z])', '_', field_name).lower()
                    if col_name not in columns:
                        columns[col_name] = java_type
                        
                jpa_entities[table_name] = columns

# Compare
print("=== Static Analysis Report ===")
for table, sql_cols in sql_tables.items():
    if table not in jpa_entities:
        print(f"[MISSING IN JPA] Table: {table}")
        continue
    
    jpa_cols = jpa_entities[table]
    for col, ctype in sql_cols.items():
        if col not in jpa_cols:
            print(f"[{table}] Missing column in JPA: {col} ({ctype})")
            
    for col, jtype in jpa_cols.items():
        if col not in sql_cols:
            print(f"[{table}] Missing column in SQL: {col} ({jtype})")

for table in jpa_entities:
    if table not in sql_tables:
        print(f"[MISSING IN SQL] Table: {table}")

print("=== Analysis Complete ===")
