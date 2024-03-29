import { BaseQueryFn, createApi, FetchArgs, fetchBaseQuery, FetchBaseQueryError } from '@reduxjs/toolkit/query/react';
import { IEvent, ISortTicketsType, ITicketType, ITicketVenueReqType } from './types/ITicketType.ts';
const dynamicBaseQuery: BaseQueryFn<string | FetchArgs, unknown, FetchBaseQueryError> = async (
    args,
    WebApi,
    extraOptions,
    // eslint-disable-next-line require-await
) => {
    let baseUrl = 'http://localhost:8080/api/v1/';

    try {
        const isChanging = args?.url?.split('/')?.find((el) => el === 'booking');

        if (isChanging) {
            baseUrl = 'http://localhost:8085/booking-service/api/v1/';
        }
    } catch (error) {
        console.log(error);
    }
    const rawBaseQuery = fetchBaseQuery({ baseUrl });

    return rawBaseQuery(args, WebApi, extraOptions);
};

function padTo2Digits(num: number) {
    return num.toString().padStart(2, '0');
}

function formatDate(date: Date) {
    return (
        [date.getFullYear(), padTo2Digits(date.getMonth() + 1), padTo2Digits(date.getDate())].join('-') +
        ' ' +
        [padTo2Digits(date.getHours()), padTo2Digits(date.getMinutes()), padTo2Digits(date.getSeconds())].join(':')
    );
}

export const ticketApi = createApi({
    reducerPath: 'ticketApi',
    tagTypes: ['Ticket'],
    baseQuery: dynamicBaseQuery,
    endpoints: (builder) => ({
        getTickets: builder.query<(ITicketType & IEvent)[], ISortTicketsType>({
            query: (data) => {
                if (data.id) {
                    return `tickets/${data.id}`;
                }
                const page = data.page ? `page=${data.page}&` : '';
                const size = data.page_size ? `page_size=${data.page_size}&` : '';
                const sort = data.sort ? `sort=${data.sort}&` : '';
                const filter = data.filter ? `filter=${data.filter}&` : '';

                return `tickets?` + page + size + sort + filter;
            },
            providesTags: (result) =>
                result
                    ? [...result.map(({ id }) => ({ type: 'Ticket' as const, id })), { type: 'Ticket', id: 'LIST' }]
                    : [{ type: 'Ticket', id: 'LIST' }],
        }),
        addTicket: builder.mutation<ITicketType, Omit<ITicketType, 'id'>>({
            query: (body) => ({
                url: `tickets`,
                method: 'POST',
                body: {
                    ...body,
                    event: {
                        name: 'event',
                    },
                    // creationDate: formatDate(new Date()),
                },
            }),
            invalidatesTags: [{ type: 'Ticket', id: 'LIST' }],
        }),
        deleteTicket: builder.mutation<void, number>({
            query: (id) => ({
                url: `tickets/${id}`,
                method: 'DELETE',
            }),
            invalidatesTags: [{ type: 'Ticket', id: 'LIST' }],
        }),
        updateTicketForm: builder.mutation<ITicketType, ITicketType>({
            query: (body) => {
                const removeEmpty: object = (obj: object) => {
                    return Object.fromEntries(
                        Object.entries(obj)
                            .filter(([_, v]) => v !== '')
                            .map(([k, v]) => [k, v === Object(v) ? removeEmpty(v) : v]),
                    );
                };

                return {
                    url: `tickets/${body.id}`,
                    method: 'PUT',
                    body: {
                        ...removeEmpty(body),
                    },
                };
            },
            invalidatesTags: [{ type: 'Ticket', id: 'LIST' }],
        }),
        getVenueAmount: builder.mutation<number, ITicketVenueReqType>({
            query: (data) => ({
                url: `tickets/venue/amount?name=${data.name}&capacity=${data.capacity}&type=${data.type}`,
                method: 'GET',
            }),
        }),
        getUniquePrices: builder.mutation<number[], void>({
            query: () => ({
                url: `tickets/prices/unique`,
                method: 'GET',
            }),
        }),
        deleteByVenue: builder.mutation<void, ITicketVenueReqType>({
            query: (body) => ({
                url: `tickets/venue`,
                method: 'DELETE',
                body,
            }),
            invalidatesTags: [{ type: 'Ticket', id: 'LIST' }],
        }),
        deleteEvent: builder.mutation<void, number>({
            query: (id) => ({
                url: `/booking/event/${id}/cancel`,
                method: 'DELETE',
            }),
            invalidatesTags: [{ type: 'Ticket', id: 'LIST' }],
        }),
        deletePerson: builder.mutation<void, number>({
            query: (id) => ({
                url: `/booking/person/${id}/cancel`,
                method: 'DELETE',
            }),
            invalidatesTags: [{ type: 'Ticket', id: 'LIST' }],
        }),
    }),
});
export const {
    useGetTicketsQuery,
    useAddTicketMutation,
    useDeleteTicketMutation,
    useUpdateTicketFormMutation,
    useGetVenueAmountMutation,
    useGetUniquePricesMutation,
    useDeleteByVenueMutation,
    useDeleteEventMutation,
    useDeletePersonMutation,
} = ticketApi;
