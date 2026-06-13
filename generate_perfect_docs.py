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
        f.write("## 1. The Sovereign AI Agent\n")
        f.write("- **Endpoint:** `POST /api/v1/agent/chat` -> Returns `sessionId`.\n")
        f.write("- **Polling:** Use `GET /api/v1/agent/sessions/{sessionId}` to poll until `status == COMPLETED`.\n")
        f.write("- **Decisions Audit:** Use `GET /api/v1/agent/sessions/{sessionId}/decisions` to show the Agent's reasoning.\n\n")
        
        f.write("## 2. Multi-Armed Bandit (MAB) Dashboards\n")
        f.write("- **Endpoint:** `GET /api/v1/campaigns/{id}/variants/mab-stats`\n")
        f.write("- **UI:** Display real-time variant performance, impressions, and shifting traffic weights.\n\n")

        f.write("## 3. Autonomous Campaign Proposals (Tinder-UI)\n")
        f.write("- **Endpoint:** `GET /api/v1/campaigns/proposals`\n")
        f.write("- **UI:** Show cards of AI-generated campaigns. Click \"Approve\" to call `POST /api/v1/campaigns/{id}/approve`.\n\n")
        
        f.write("## 4. Omni-Awareness Fatigue (Sleep Agent)\n")
        f.write("- **UI:** Automatically enforced by the backend (`channel_cooldown_until`). Display suppressed users in campaign stats.\n\n")
        
        f.write("## 5. Organizational Memory\n")
        f.write("- **Endpoint:** `GET /api/v1/memory`\n")
        f.write("- **UI:** Show actionable insights learned from past campaigns.\n\n")

        f.write("## 6. AGI Testing Panel\n")
        f.write("- **Endpoints:** `POST /api/v1/test/agi/*`\n")
        f.write("- **UI:** Create buttons to trigger War Room, Fund Manager, and Omni-Awareness background jobs instantly.\n\n")

    print(f"Generated successfully to {OUTPUT_FILE}")

if __name__ == "__main__":
    main()
