# Tracking Number Generator API

## Endpoint

**GET /next-tracking-number**

### Query Parameters:

| Parameter             | Type          | Description                                          | Example                              |
|-----------------------|---------------|------------------------------------------------------|------------------------------------|
| `origin_country_id`    | String (ISO 3166-1 alpha-2) | Origin country code of the order                   | `US`                               |
| `destination_country_id` | String (ISO 3166-1 alpha-2) | Destination country code of the order             | `IN`                               |
| `weight`              | Decimal       | Weight of the parcel in kilograms (up to 3 decimals) | `2.345`                            |
| `created_at`          | DateTime (RFC 3339) | Timestamp when the order was created                 | `2025-05-22T10:00:00Z`             |
| `customer_id`         | UUID          | Unique identifier of the customer                     | `de619854-b59b-425e-9db4-943979e1bd49` |
| `customer_name`       | String        | Full name of the customer                             | `Test Logistics`                   |
| `customer_slug`       | String        | Customer name in slug-case (kebab-case)              | `test-logistics`                   |

---

### Sample Request

#### Using curl:

```bash
curl -X GET "http://localhost:8080/next-tracking-number?origin_country_id=US&destination_country_id=IN&weight=2.345&created_at=2025-05-22T10:00:00Z&customer_id=de619854-b59b-425e-9db4-943979e1bd49&customer_name=Test%20Logistics&customer_slug=test-logistics"
