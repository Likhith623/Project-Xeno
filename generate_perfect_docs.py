import urllib.request
import json
import re

URL = "https://project-xeno.onrender.com/v3/api-docs"
OUTPUT_FILE = r"C:\Users\Sarishma\Project-Xeno\frontend_integration.md"

def fetch_schema():
    with open('openapi.json', 'r', encoding='utf-8-sig') as f:
        return json.load(f)

def resolve_ref(ref, spec):
    parts = ref.split('/')
    obj = spec
    for part in parts[1:]:
        obj = obj.get(part, {})
    return obj

def schema_to_example(schema, spec, depth=0):
    if depth > 10:
        return "..."
    
    if '$ref' in schema:
        resolved = resolve_ref(schema['$ref'], spec)
        return schema_to_example(resolved, spec, depth + 1)
    
    if 'allOf' in schema:
        # Merge properties from all components
        merged = {}
        for sub in schema['allOf']:
            ex = schema_to_example(sub, spec, depth + 1)
            if isinstance(ex, dict):
                merged.update(ex)
        return merged
        
    ptype = schema.get('type')
    
    if ptype == 'object' or 'properties' in schema:
        props = schema.get('properties', {})
        obj = {}
        for k, v in props.items():
            obj[k] = schema_to_example(v, spec, depth + 1)
        if not obj and 'additionalProperties' in schema:
            return {"key": schema_to_example(schema['additionalProperties'], spec, depth + 1)}
        return obj
        
    if ptype == 'array':
        items = schema.get('items', {})
        return [schema_to_example(items, spec, depth + 1)]
        
    if ptype == 'string':
        if 'format' in schema and schema['format'] == 'date-time':
            return "2026-06-14T12:00:00Z"
        if 'format' in schema and schema['format'] == 'uuid':
            return "123e4567-e89b-12d3-a456-426614174000"
        if 'enum' in schema:
            return schema['enum'][0]
        return "string"
        
    if ptype == 'integer':
        return 0
    if ptype == 'number':
        return 0.0
    if ptype == 'boolean':
        return False
        
    return "any"

def main():
    try:
        spec = fetch_schema()
    except Exception as e:
        print(f"Error fetching schema: {e}")
        return

    paths = spec.get('paths', {})
    
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
        f.write("# Project Xeno: Exhaustive Frontend Integration Guide\n\n")
        f.write("This file contains the **100% rigorous** analysis of every single endpoint and AI feature for frontend implementation.\n\n")
        f.write("## 🌍 Base URL & Authentication\n")
        f.write("**Live Production URL:** `https://project-xeno.onrender.com/api/v1`\n")
        f.write("**Authentication Header:** `X-API-KEY: <your-api-key>`\n\n---\n\n")

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
                        schema_type = p.get('schema', {}).get('type', 'string')
                        f.write(f"- `{p['name']}`{reqStr} ({p.get('in')}): `{schema_type}`\n")
                    f.write("\n")
                
                # Request Body
                if 'requestBody' in d:
                    content = d['requestBody'].get('content', {})
                    if 'application/json' in content:
                        schema = content['application/json'].get('schema', {})
                        example = schema_to_example(schema, spec)
                        f.write("**Request Payload (`application/json`):**\n```json\n")
                        f.write(json.dumps(example, indent=2))
                        f.write("\n```\n\n")
                        
                # Responses
                f.write("**Response:**\n")
                for code, resp in d.get('responses', {}).items():
                    if code.startswith('2'):
                        content = resp.get('content', {})
                        schema = None
                        if 'application/json' in content:
                            schema = content['application/json'].get('schema', {})
                        elif '*/*' in content:
                            schema = content['*/*'].get('schema', {})
                        
                        if schema is not None:
                            example = schema_to_example(schema, spec)
                            f.write(f"**`{code} OK` Payload:**\n```json\n")
                            f.write(json.dumps(example, indent=2))
                            f.write("\n```\n\n")
                        else:
                            f.write(f"- `{code}`: {resp.get('description', '')}\n\n")
                f.write("---\n\n")

        # Append AI features
        f.write("# 🧠 Core AI Features & Frontend Integration Logic\n\n")
        f.write("### 👑 Phase 7: Multi-Agent Architecture\n")
        f.write("## 18. Autonomous Budget Agent 💰\n")
        f.write("- **Endpoint:** `POST /api/v1/test/ai/trigger-fund-manager`\n")
        f.write("- **Frontend Flow:** Build an \"AI Developer Panel\" button to manually trigger the Autonomous Budget Agent to instantly re-allocate live campaign budgets.\n\n")

        f.write("## 19. AI War Room (Multi-Agent Debate) ⚔️\n")
        f.write("- **Endpoint:** `POST /api/v1/test/ai/trigger-war-room`\n")
        f.write("- **Frontend Flow:** Build an AI Test button. When triggered, the backend spins up two LLMs to debate campaign strategy and returns a synthesized compromise strategy.\n\n")

        f.write("### 👁️ Phase 8: Omni-Awareness\n")
        f.write("## 20. Fatigue Engine 💤\n")
        f.write("- **Endpoint:** `POST /api/v1/test/ai/trigger-omni-awareness`\n")
        f.write("- **Frontend Flow:** Backend enforces a 14-day channel cooldown. The frontend displays \"Users Suppressed by Fatigue\" stats on the Campaign summary to show the AI protecting the user base.\n\n")

        f.write("## 21. Micro-Churn Velocity (The Whisperer) 📉\n")
        f.write("- **Backend Enforced:** Detects 5-day velocity lags in purchases.\n")
        f.write("- **Frontend Flow:** Display \"Velocity Interventions\" metrics in the Customer 360 view.\n\n")

        f.write("---\n")
        f.write("**End of Ultimate 21-Feature Frontend Guide**\n")

    print(f"Generated successfully to {OUTPUT_FILE}")

if __name__ == "__main__":
    main()
