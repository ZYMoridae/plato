SET CLIENT_MIN_MESSAGES = WARNING;BEGIN;alter table public.order_item    add column sku_code varchar(200);alter table public.cart_item    add column sku_code varchar(200);DROP TABLE IF EXISTS public.user_log;CREATE TABLE public.user_log(    id         serial,    action     text                  not null,    user_id    int                   NOT NULL,    ip_addr    character varying(50) NOT NULL,    created_at timestamp without time zone DEFAULT now(),    updated_at timestamp without time zone DEFAULT now(),    CONSTRAINT user_log_pkey PRIMARY KEY (id));INSERT INTO public.order_status    (id, "name", created_at, updated_at)VALUES (4, 'token_expired', '2019-04-03 19:00:03.088', '2019-04-03 19:00:03.088');DROP TABLE IF EXISTS public.currency_rates;CREATE TABLE public.currency_rates(    id            serial,    currency_code character varying(5) NOT NULL,    base_currency character varying(5) NOT NULL,    rate          double precision     NOT NULL,    created_at    timestamp without time zone DEFAULT now(),    updated_at    timestamp without time zone DEFAULT now(),    CONSTRAINT currency_rates_pkey PRIMARY KEY (id),    CONSTRAINT currency_code_base_currency_ukey UNIQUE (currency_code, base_currency));DROP TABLE IF EXISTS public.class;CREATE TABLE public.class(    id                serial,    name              text             not null,    thumbnail         text,    description       text             not null,    unit              text             not null,    unit_price        double precision not null,    class_category_id int              NOT NULL REFERENCES "class_category" (id),    created_at        timestamp without time zone DEFAULT now(),    updated_at        timestamp without time zone DEFAULT now(),    teacher_id        int              NOT NULL REFERENCES "user" (id),    CONSTRAINT class_pkey PRIMARY KEY (id));DROP TABLE IF EXISTS public.teacher_available_time;CREATE TABLE public.teacher_available_time(    id          serial,    teacher_id  int NOT NULL REFERENCES "user" (id),    start_time  timestamp without time zone DEFAULT now(),    end_time    timestamp without time zone DEFAULT now(),    created_at  timestamp without time zone DEFAULT now(),    updated_at  timestamp without time zone DEFAULT now(),    is_reserved boolean                     default false,    CONSTRAINT teacher_available_time_pkey PRIMARY KEY (id));DROP TABLE IF EXISTS public.class_order;CREATE TABLE public.class_order(    id          serial,    user_id     int              NOT NULL REFERENCES "user" (id),    invoice_id  text,    total_price double precision NOT NULL,    created_at  timestamp without time zone DEFAULT now(),    updated_at  timestamp without time zone DEFAULT now(),    status_id   int              NOT NULL REFERENCES "order_status" (id),    CONSTRAINT class_order_pkey PRIMARY KEY (id));DROP TABLE IF EXISTS public.class_order_item;CREATE TABLE public.class_order_item(    id                        serial,    class_order_id            int              NOT NULL REFERENCES "class_order" (id),    class_id                  int              NOT NULL REFERENCES "class" (id),    teacher_available_time_id int              NOT NULL REFERENCES "teacher_available_time" (id),    price                     double precision NOT NULL,    created_at                timestamp without time zone DEFAULT now(),    updated_at                timestamp without time zone DEFAULT now(),    CONSTRAINT class_order_item_pkey PRIMARY KEY (id));DROP TABLE IF EXISTS public.class_cart;CREATE TABLE public.class_cart(    id         serial,    user_id    int NOT NULL REFERENCES "user" (id),    created_at timestamp without time zone DEFAULT now(),    updated_at timestamp without time zone DEFAULT now(),    CONSTRAINT class_cart_pkey PRIMARY KEY (id));DROP TABLE IF EXISTS public.class_cart_item;CREATE TABLE public.class_cart_item(    id                        serial,    class_cart_id             int              NOT NULL REFERENCES "class_cart" (id),    class_id                  int              NOT NULL REFERENCES "class" (id),    teacher_available_time_id int              NOT NULL REFERENCES "teacher_available_time" (id),    price                     double precision NOT NULL,    created_at                timestamp without time zone DEFAULT now(),    updated_at                timestamp without time zone DEFAULT now(),    CONSTRAINT class_cart_item_pkey PRIMARY KEY (id));DROP TABLE IF EXISTS public.invoice;CREATE TABLE public.invoice(    id          serial,    entity_id   integer              NOT NULL,    entity_type character varying(5) NOT NULL,    created_at  timestamp without time zone DEFAULT now(),    updated_at  timestamp without time zone DEFAULT now(),    invoice_id  text                 NOT NULL,    CONSTRAINT invoice_pkey PRIMARY KEY (id),    CONSTRAINT invoice_invoice_id_key UNIQUE (invoice_id));DROP TABLE IF EXISTS public.class_category;CREATE TABLE public.class_category(    id         serial,    name       character varying(50) NOT NULL,    code       character varying(10) NOT NULL,    created_at timestamp without time zone DEFAULT now(),    updated_at timestamp without time zone DEFAULT now(),    CONSTRAINT class_category_pkey PRIMARY KEY (id),    CONSTRAINT class_category_code_key UNIQUE (code));alter table class add column thumbnail text;COMMIT;