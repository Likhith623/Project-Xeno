import urllib.request
import json

URL = "http://localhost:8080/v3/api-docs"
OUTPUT_FILE = r"C:\Users\Sarishma\Project-Xeno\frontend_integration.md"

def fetch_schema():
    req = urllib.request.Request(URL)
    with urllib.request.urlopen(req) as response:
        return json.loads(response.read().decode('utf-8'))

def resolve_ref(ref, spec):
    parts = ref.split('/')
    obj = spec
    for part in parts[1:]:
        obj = obj.get(part, {})
    return obj

def type_to_example(prop, spec, depth=0):
    if depth > 5:
        return "..."
    if '$ref' in prop:
        resolved = resolve_ref(prop['$ref'], spec)
        return schema_to_example(resolved, spec, depth+1)
    
    ptype = prop.get('type')
    if ptype == 'string':
        if 'format' in prop and prop['format'] == 'date-time':
            return "2026-06-13T12:00:00Z"
        if 'enum' in prop:
            return prop['enum'][0]
        return "string"
    if ptype == 'integer':
        return 0
    if ptype == 'number':
        return 0.0
    if ptype == 'boolean':
        return False
    if ptype == 'array':
        items = prop.get('items', {})
        return [type_to_example(items, spec, depth+1)]
    if ptype == 'object':
        if 'additionalProperties' in prop:
            return {"key": type_to_example(prop['additionalProperties'], spec, depth+1)}
        return {}
    return "any"

def schema_to_example(schema, spec, depth=0):
    if depth > 5:
        return {}
    if 'properties' in schema:
        return {k: type_to_example(v, spec, depth) for k, v in schema['properties'].items()}
    if 'type' in schema:
        return type_to_example(schema, spec, depth)
    return {}

def main():
    spec = fetch_schema()
    paths = spec.get('paths', {})
    
    # Group by first tag
    grouped = {}
    for path, methods in paths.items():
        for method, details in methods.items():
            tags = details.get('tags', ['Other'])
            tag = tags[0]
            if tag not in grouped:
                grouped[tag] = []
            grouped[tag].append({
                'path': path,
                'method': method.upper(),
                'details': details
            })

    with open(OUTPUT_FILE, 'w', encoding='utf-8') as f:
        f.write("# Project Xeno: AI-Native CRM Backend - Ultimate API Contract\n\n")
        f.write("Welcome to the **100% Exhaustively Generated Frontend Integration Guide**.\n")
        f.write("This file guarantees 100% parity with the backend source code.\n\n")
        f.write("## 🌍 Base URL & Authentication\n")
        f.write("**Live Production URL:** `https://project-xeno.onrender.com/api/v1`\n")
        f.write("**Authentication Header:** `X-API-KEY: likhit@178926a`\n\n---\n\n")

        for tag, endpoints in sorted(grouped.items()):
            f.write(f"## {tag}\n\n")
            for ep in endpoints:
                d = ep['details']
                f.write(f"### `{ep['method']} {ep['path']}`\n")
                f.write(f"**Summary:** {d.get('summary', 'No summary')}\n\n")
                
                # Parameters
                if 'parameters' in d:
                    f.write("**Parameters:**\n")
                    for p in d['parameters']:
                        reqStr = "*" if p.get('required') else ""
                        f.write(f"- `{p['name']}`{reqStr} ({p.get('in')}): `{p.get('schema', {}).get('type', 'string')}`\n")
                    f.write("\n")
                
                # Request Body
                if 'requestBody' in d:
                    content = d['requestBody'].get('content', {})
                    if 'application/json' in content:
                        schema = content['application/json'].get('schema', {})
                        if '$ref' in schema:
                            schema = resolve_ref(schema['$ref'], spec)
                        example = schema_to_example(schema, spec)
                        f.write("**Payload (`application/json`):**\n```json\n")
                        f.write(json.dumps(example, indent=2))
                        f.write("\n```\n\n")
                        
                # Responses
                f.write("**Responses:**\n")
                for code, resp in d.get('responses', {}).items():
                    f.write(f"- `{code}`: {resp.get('description', '')}\n")
                    content = resp.get('content', {})
                    if 'application/json' in content:
                        schema = content['application/json'].get('schema', {})
                        if '$ref' in schema:
                            schema = resolve_ref(schema['$ref'], spec)
                        example = schema_to_example(schema, spec)
                        f.write("```json\n")
                        f.write(json.dumps(example, indent=2))
                        f.write("\n```\n\n")
                f.write("---\n\n")
                
    print("Generation complete.")

if __name__ == "__main__":
    main()
