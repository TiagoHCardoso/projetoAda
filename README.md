```powershell
curl -i -X POST http://localhost:9091/agendamento \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Reunião de planejamento",
    "dataInicio": "2026-11-10",
    "horaInicio": "09:00:00",
    "dataFim": "2026-11-10",
    "horaFim": "10:30:00"
  }'
```

```powershell
curl -i -X GET http://localhost:9091/agendamento/1 \
  -H "Accept: application/json"
```

```powershell
curl -i -X GET "http://localhost:9091/agendamento?nome=Reuni" \
  -H "Accept: application/json"
```

```powershell
curl -i -X DELETE http://localhost:9091/agendamento/1 \
  -H "Accept: application/json"
```

```powershell
curl -i -X PUT http://localhost:9091/agendamento/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Reunião de planejamento",
    "dataInicio": "2026-11-10",
    "horaInicio": "09:00:00",
    "dataFim": "2026-11-10",
    "horaFim": "10:30:00"
  }'
```

```powershell
curl -i -X POST http://localhost:9091/agendamento \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Reunião de planejamento",
    "dataInicio": "2026-11-10",
    "horaInicio": "10:31:00",
    "dataFim": "2026-11-10",
    "horaFim": "10:33:00"
  }'
```

```powershell
curl -i -X PUT http://localhost:9091/agendamento/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Reunião de planejamento",
    "dataInicio": "2026-11-10",
    "horaInicio": "10:31:00",
    "dataFim": "2026-11-10",
    "horaFim": "10:33:00"
  }'
```