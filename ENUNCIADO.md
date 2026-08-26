# Enunciado / material de partida — Filósofos comensales

Práctica del bloque **Procesos e Hilos** de Programación de Servicios y Procesos basada en el problema clásico de los filósofos comensales.

Varios filósofos se sientan alrededor de una mesa y alternan entre pensar y comer. Entre cada pareja de filósofos existe un tenedor/recurso compartido y, para comer, cada filósofo necesita disponer simultáneamente de los dos tenedores adyacentes.

La implementación debe coordinar el acceso concurrente a los tenedores de forma que dos filósofos no utilicen el mismo recurso a la vez y, especialmente, debe evitar que el sistema quede bloqueado permanentemente por espera circular.

La versión moderna del repositorio utiliza bloqueos independientes y un orden total de adquisición para eliminar el interbloqueo.

No existe una base de datos asociada.
