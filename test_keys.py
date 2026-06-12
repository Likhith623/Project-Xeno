import smtplib
import requests
import sys

# Test SMTP
try:
    server = smtplib.SMTP('smtp.gmail.com', 587)
    server.starttls()
    server.login('kingjames.08623@gmail.com', 'kngx xjhy ogro ycps')
    print('SMTP: WORKING')
    server.quit()
except Exception as e:
    print('SMTP: FAILED -', e)

# Test Gemini API Key
api_key = 'AQ.Ab8RN6KD9rE2Mbz9aR8LSLtbNEOGQwIivMJB3EmfB2qYLkzv3Q'
url = f'https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key={api_key}'
data = {
    'contents': [{'parts':[{'text': 'Hello'}]}]
}
response = requests.post(url, json=data)
if response.status_code == 200:
    print('GEMINI API: WORKING')
else:
    print('GEMINI API: FAILED -', response.status_code, response.text)
