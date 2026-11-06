# dia de feriado

```powershell
curl -i -X POST http://localhost:9091/agendamento \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Reunião de planejamento",
    "dataInicio": "2026-12-06",
    "horaInicio": "09:00:00",
    "dataFim": "2026-11-10",
    "horaFim": "10:30:00"
  }'
```

# agendamento simples

agendar, buscar, deletar

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
curl -i -X DELETE http://localhost:9091/agendamento/1 \
  -H "Accept: application/json"
```

# agendamento com edicao

agendar com mesmo horario 2x (erro), agendar horarios distintos 2x, editar para o mesmo horario no mesmo agendamento, editar para o mesmo horario de um agendamento diferente (erro)


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

(erro)
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
    "horaInicio": "09:00:00",
    "dataFim": "2026-11-10",
    "horaFim": "10:30:00"
  }'
```
(erro)
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








```powershell
curl -i -X GET "http://localhost:9091/agendamento?nome=Reuni" \
  -H "Accept: application/json"
```



```powershell
curl -i -X GET "http://localhost:9091/q/metrics" \
  -H "Accept: application/json"
```

| Métrica | Tipo | Finalidade |
|---|---|---|
| `feriado.consultas` | Counter | Quantidade de consultas |
| `feriado.consultas.sucesso` | Counter | Consultas concluídas com sucesso |
| `feriado.consultas.erro` | Counter | Consultas que falharam |
| `feriado.consultas.tempo` | Timer | Tempo de resposta da API |
| `feriado.retorno.quantidade` | DistributionSummary | Quantidade de feriados retornados por consulta |

| Métrica | Tipo | O que monitora |
|---|---|---|
| `agendamento.criados` | Counter | Agendamentos criados com sucesso |
| `agendamento.atualizados` | Counter | Agendamentos atualizados com sucesso |
| `agendamento.excluidos` | Counter | Agendamentos excluídos |
| `agendamento.rejeitados` | Counter | Operações rejeitadas por conflito de horário ou feriado |
| `agendamento.consultas` | Counter | Consultas de agendamentos |
| `agendamento.operacao.tempo` | Timer | Tempo gasto nas operações |


docker logs catalogo-api-catalogo-api-1